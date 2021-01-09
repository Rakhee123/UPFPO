<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix='form' uri='http://www.springframework.org/tags/form'%>
<spring:url value="/resources/assets" var="resourceurl" />
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page buffer="8kb" %>

	
<div class="container" style="width:100%">
	<div style = "max-width:500px;  margin: auto; background-color:#F3F0F0; padding: 20px;">
	<br>				<br/>
				<h5 style="padding:10px;" class="btn-warning"><spring:message code="login.login" text="Login"/></h5>
				<hr/>
				<label style="color: red">${error} ${changed} ${errors}</label>
									
					<form:form action="login" modelAttribute="users" method="post" id="login-form">
					<input type="hidden" name="token"  value="${csrf_token}"/>
						
					<!-- 	<div class="form-group" >
							<label>Sign in as</label>
							<select class="form-control input-sm" tabindex="1" path="slaRefId" id="slaref">
												 <option value="">Select User Type </option>
												 <option value="0">Farmer </option> 
												<option value="1">FPO </option>
												<option value="2">Agriculture Department</option>
												<option value="3">Buyer/ Seller </option>
												<option value="4">Labour Contractor </option>
												<option value="5">Equipment Owner </option>
												<option value="6">Equipment Renter </option>
												
											</select>
											
						</div> -->
						<div class="form-group" >
							<label><b><spring:message code="login.user" text="Username"/></b></label>
							<form:input path="userName" class="form-control input-sm" autocomplete="off"
								placeholder="Username" />
								<form:errors path="userName" style="color: red"/>
						</div>
			
						<div class="form-group">
							<label><b><spring:message code="login.password" text="Password"/></b></label>
							<form:input class="form-control input-sm" placeholder="******"  autocomplete="off"
								type="password" path="password" id="password"   onblur="return encryptPass();" />
								<form:errors path="password" style="color: red"/>
						</div>
						
						<div class="form-group">
							<!-- <a class="float-right" href="#">Forgot?</a> -->
								<img src="<c:url value="/captcha.jpg" />" style="height:47px;width:210px;"  alt="captcha" id="captchaImg"/><span> &nbsp;&nbsp;&nbsp;&nbsp;<img class="captcha" src="${pageContext.request.contextPath}/resources/images/refresh-round-symbol.png" height="40px" onclick="return reloadCaptcha();">  </span>
									<br/>
									<label><b><spring:message code="login.captcha" text="Enter the text shown in above image"/></b></label><br/>
								<form:input class="form-control input-sm" placeholder="Captcha Code" 
									path="captcha" id="captchaId" maxlength="6"  />
									<form:errors path="captcha" style="color: red" onkeypress="return checkChar(event);"/>
									<p style="color: red">${error_captcha}</p>
						</div>
						
						<div class="row">
						<div class="col-sm-6"></div>
						<div class="col-sm-6 pull-right">
							<a href="${pageContext.request.contextPath}/forgotPass" class="txt3">
								<spring:message code="login.forgot" text="Forgot password"/>?
							</a>
						</div>
					</div>
						
						<div class="row">
						<div class="col-sm-6">
							<button type="submit" class="btn btn-success btn-block input-sm" id="form_submit">
								<spring:message code="login.login" text="Login"/></button> 
						</div>
						<div class="col-sm-6 pull-right">
						<a class="text-primary bg-success" href="${pageContext.request.contextPath}/">
						<button type="button" class="btn btn-warning btn-block input-sm"><spring:message code="login.home" text="Go to Home"/></button>
						</a>
						</div>
						</div>
						<hr/>
					</form:form>
					<!-- <label style="color: red"></label>
					<label style="color: red"></label> -->
				
			</div>
             <div>
			</div>	
		</div>
 
 <script type="text/javascript">
  
  function reloadCaptcha(){
	    var d = new Date();
	    $("#captchaImg").attr("src", '<c:url value="/captcha.jpg?"/>'+d.getTime());
	}	
   
   $(document).ready(function(){
	   $('#captchaId').val("");
  });
  
 </script>
 
 <script type="text/javascript">
 
 function encryptPass() {
	 
		var pass = $('#password').val();
		$.ajax({
			url : 'encryptpass',
			type : 'GET',
			data : {
				'pass' : pass
			},
			success : function(data, status) {
				var j = JSON.parse(data);
				if (j == null) {
					$("#password").empty();
				} else {
					$("#password").empty();
					var newPass = '<input class="form-control input-sm" id="pass"  />';
					newPass = j;
					document.getElementById("password").value=newPass;
				}
			},
			error : function(data, status) {
			}
		});
	}
 </script> 
 
