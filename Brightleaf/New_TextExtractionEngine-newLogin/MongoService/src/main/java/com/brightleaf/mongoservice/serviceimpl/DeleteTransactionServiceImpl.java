package com.brightleaf.mongoservice.serviceimpl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.apache.log4j.Logger;
import org.bson.Document;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.brightleaf.mongoservice.model.UserQC;
import com.brightleaf.mongoservice.repository.UserQcRepository;
import com.brightleaf.mongoservice.service.DeleteTransactionService;
import com.mongodb.MongoClient;
import com.mongodb.client.ListCollectionsIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

@Service
public class DeleteTransactionServiceImpl implements DeleteTransactionService {

	protected final static Logger logger = Logger.getLogger(DeleteTransactionServiceImpl.class);

	@Autowired
	UserQcRepository transactionRepository;

	private String companyName;
	private String transactionDeletedBy;
	private String transactionId;

	@Override
	public String deleteTransaction(JSONObject content) {

		companyName = content.getString("companyName");
		transactionId = content.getString("transactionId");
		transactionDeletedBy = content.getString("transactionDeletedBy");

		try (MongoClient mongo = new MongoClient("localhost", 27017)) {
//			List<String> jhdcw = mongo.getDB(companyName).;
			MongoDatabase db = mongo.getDatabase(companyName);
			// check if we have collection with this transaction id present in database or
			// not
			boolean collectionExistsIf = db.listCollectionNames().into(new ArrayList<String>()).contains(transactionId);
			if (collectionExistsIf == true) {
				MongoCollection<Document> collection = db.getCollection(transactionId);

				// if collection named DeletedTransactonCollection doesn't exists than create
				boolean collectionExists = db.listCollectionNames().into(new ArrayList<String>())
						.contains("DeletedTransactionCollection");
				if (collectionExists == false) {
					db.createCollection("DeletedTransactionCollection");
				}

				// putting in DeletedTransactonCollection
				updateDeleteTransactionCollection(companyName, transactionId, transactionDeletedBy, db);

				// delete form Mongo
				collection.drop();

				// Deleting from Sql
				List<UserQC> allTransactions = transactionRepository.getTransactions(transactionId);
				transactionRepository.deleteInBatch(allTransactions);
			}
		} catch (Exception e) {
			logger.error("Exception while deleting the collection", e);
		}
		return "Transaction deleted successfully";
	}

	@Override
	public String deleteTransactionsCompanywise(JSONObject content, Integer companyId) {

		companyName = content.getString("companyName");
		transactionDeletedBy = content.getString("transactionDeletedBy");

		try (MongoClient mongo = new MongoClient("localhost", 27017)) {
			MongoDatabase db = mongo.getDatabase(companyName);

			// Get list of collections
			ListCollectionsIterable<Document> it = db.listCollections();
			MongoCursor<Document> it1 = it.iterator();

			// iterate through all collections
			while (it1.hasNext()) {
				Document item = it1.next();
				String name = item.getString("name");
				// check if Collection name has the TX_comapnyId as we don't want to delete other
				// collections
				if (name.contains("TXN_" + companyId)) {

					// if collection named DeletedTransactonCollection doesn't exists than create
					boolean collectionExists = db.listCollectionNames().into(new ArrayList<String>())
							.contains("DeletedTransactionCollection");
					if (collectionExists == false) {
						db.createCollection("DeletedTransactionCollection");
					}

					// Put the deleted transaction in the deletedTransactioCollection
					updateDeleteTransactionCollection(companyName, name, transactionDeletedBy, db);

					db.getCollection(name).drop();

					// Deleting from Sql
					List<UserQC> allTransactions = transactionRepository.getTransactions(name);
					transactionRepository.deleteInBatch(allTransactions);
				}
			}
		} catch (Exception e) {
			logger.error("Exception while deleting the collection", e);
		}
		return "Transactions in a company deleted successfully";
	}

	@Override
	public String deleteTransactionsDatewise(JSONObject content) {
		companyName = content.getString("companyName");
		transactionDeletedBy = content.getString("transactionDeletedBy");
		String startDate = content.getString("startDate");
		String endDate = content.getString("endDate");
		
		System.out.println("startDate"+ startDate);
		System.out.println("endDate" + endDate);
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try (MongoClient mongo = new MongoClient("localhost", 27017)) {
			Date dateStart = sdf.parse(startDate);
			Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
			cal.setTime(dateStart);
			long timeStart = cal.getTimeInMillis();

			SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
			Date dateEnd = sdf1.parse(endDate);
			Calendar cal1 = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
			cal1.setTime(dateEnd);
			cal1.add(Calendar.DATE, 1);
			cal1.set(Calendar.HOUR_OF_DAY, cal1.getMaximum(Calendar.HOUR_OF_DAY));
			cal1.set(Calendar.MINUTE, cal1.getMaximum(Calendar.MINUTE));
			cal1.set(Calendar.SECOND, cal1.getMaximum(Calendar.SECOND));
			cal1.set(Calendar.MILLISECOND, cal1.getMaximum(Calendar.MILLISECOND));
			long timeEnd = cal1.getTimeInMillis();

			System.out.println("timeStart"+ timeStart);
			System.out.println("timeEnd" + timeEnd);
			
			// Checking now for transaction id
			MongoDatabase db = mongo.getDatabase(companyName);
			ListCollectionsIterable<Document> it = db.listCollections();
			MongoCursor<Document> it1 = it.iterator();

			// iterate through all collections
			while (it1.hasNext()) {
				Document item = it1.next();
				String name = item.getString("name");
				if (name.contains("_")) {
					String stringTime = name.substring(name.lastIndexOf("_") + 1);
					long stringTimeDouble = Long.parseLong(stringTime);
					System.out.println("start" + timeStart);
					System.out.println("end" + timeEnd);
					System.out.println("txId" + stringTimeDouble);

					if (stringTimeDouble >= timeStart && stringTimeDouble <= timeEnd) {
						System.out.println("its between the number");
						
						//
						boolean collectionExists = db.listCollectionNames().into(new ArrayList<String>())
								.contains("DeletedTransactionCollection");
						if (collectionExists == false) {
							db.createCollection("DeletedTransactionCollection");
						}
						updateDeleteTransactionCollection(companyName, name, transactionDeletedBy, db);
						
						db.getCollection(name).drop();

						// Deleting from Sql
						List<UserQC> allTransactions = transactionRepository.getTransactions(name);
						transactionRepository.deleteInBatch(allTransactions);
					}
				}
			}
		} catch (ParseException e1) {
			logger.error("Error in deleteTransactionsDatewise", e1);
		}
		return "Transactions between the Dates deleted successfully";
	}

	private void updateDeleteTransactionCollection(String companyName, String transactionId,
			String transactionDeletedBy, MongoDatabase db) {
		Date transactionDate = null;
		try {
			MongoCollection<Document> collection = db.getCollection(transactionId);
			List<Document> documents = (List<Document>) collection.find().into(new ArrayList<Document>());
			for (Document d1 : documents) {
				transactionDate = d1.getDate("TransactionDate");
				break;
			}

			Document deleteTransactionType = new Document();
			deleteTransactionType.put("TransactionId", transactionId);
			deleteTransactionType.put("TransactionDate", transactionDate);
			deleteTransactionType.put("DeletionDate", new Date());
			deleteTransactionType.put("DeletedBy", transactionDeletedBy);

			MongoCollection<Document> collection1 = db.getCollection("DeletedTransactionCollection");
			collection1.insertOne(deleteTransactionType);
		} catch (Exception e) {
			logger.error("Error in updateDeleteTransactionCollection", e);
		}
	}
}
