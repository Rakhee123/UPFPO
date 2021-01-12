package com.brightleaf.documenttypeservice.resource;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.brightleaf.documenttypeservice.model.Attribute;
import com.brightleaf.documenttypeservice.model.DocumentType;
import com.brightleaf.documenttypeservice.service.AttributeService;
import com.brightleaf.documenttypeservice.service.DocumentTypeService;

@CrossOrigin(origins = "*")
@RestController
public class AttributeDocumentResource {

	@Autowired
	private AttributeService attrService;

	@Autowired
	private DocumentTypeService docTypeService;

	// GET Attribute and documents type
	@GetMapping("/getDocAndAttribute/{attributeId}/{documentTypeId}")
	public JSONObject getDocAndAttribute(@PathVariable("attributeId") final Integer attributeId,
			@PathVariable("documentTypeId") final Integer documentTypeId) {
		Attribute attribute = attrService.getAttribute(attributeId);
		DocumentType docType = docTypeService.getDocumentType(documentTypeId);
		JSONObject obj = new JSONObject();
		obj.put("documentType", docType.getDocumentName());
		obj.put("attributeName", attribute.getAttributeName());
		return obj;
	}
}
