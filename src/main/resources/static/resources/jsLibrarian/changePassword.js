define(["jquery", "bootstrap", "additional-methods"], function($) {
	$(function() {
		$.validator.addMethod("notEqualTo", function(value, element, param) {
			 return this.optional(element) || value != $(param).val();
			 }, "This has to be different...");
		
		  $( "#changePasswordForm" ).validate({
			rules: {
			  oldPassword: {
				      required: true
				    },	
			  password: {
					required: true,
				    notEqualTo: "#oldPassword",
					minlength: 8},
			password2: {
				    required: true,
					equalTo: "#password"
			}
		  },
		  messages: {
			  "oldPassword" : {
				  required : "Please enter your current password"
			  },
			  "password" : {
				  required : "Please enter a new password",
				  notEqualTo : "New password cannot be same as the current password"
			  },
			  "password2" : {
				  required : "Please enter a new password",
				  equalTo : "Please verify that the passwords are same"
			  }
		  }
			  
		  });
	});
	/*$(function() {
		$.validator.addMethod("notEqualTo", function(value, element, param) {
			 return this.optional(element) || value != $(param).val();
			 }, "This has to be different...");
		
		$('#changePasswordForm').validate({
				  rules: {
				    "oldPassword": {
				      required: true
				    },
				    "password": {
				      required: true,
				      notEqualTo: "#oldPassword"
				    },
				    "password2": {
				      required: true,
				      equalTo : "#password"
				    },
				  },
				  messages: {
					  "oldPassword" : {
						  required : "Please enter your current password"
					  },
					  "password" : {
						  required : "Please enter a new password",
						  notEqualTo : "New password cannot be same as the current password"
					  },
					  "password2" : {
						  required : "Please enter a new password",
						  equalTo : "Please verify that the passwords are same"
					  }
				  },
				  highlight: function(element) {
				    $(element).closest('.ui-input').removeClass('success').addClass('error');
				  },
				  success: function(element) {
				    element
				    .closest('.ui-input').removeClass('error').addClass('success');
				  },
				  errorPlacement: function(error, element) {
				    error.appendTo( element.closest(".ui-input"));
				  }
				 });
	});*/
});