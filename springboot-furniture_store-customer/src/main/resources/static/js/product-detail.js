$(document).ready(function() {


	$('.input-field-quantity').on('input', function() {
		var quantity = $(this).val();
		var maxQuantity = $(this).attr("max");
		var minQuantity = $(this).attr("min");
		
		if(quantity.indexOf('.') > -1){
			var newValue = parseInt(quantity*10);
			console.log(newValue)
			quantity= newValue
			$(this).val(newValue)
		}


		if (quantity != '' && parseInt(quantity) > parseInt(maxQuantity)) {
			quantity = maxQuantity;
			$(this).val(maxQuantity);
		}
		
		if (quantity != '' && parseInt(quantity) <=0) {
			quantity = parseInt(minQuantity);
			$(this).val(quantity);
		}	
	});
	
	$('.button1quantity').on('click', function(){
		var quantity = $('.input-field-quantity').val()
		var minQuantity = $('.input-field-quantity').attr("min");
		var newQuantity = quantity - 1;
		
		
		if (newQuantity <= 0) {
       		 newQuantity = parseInt(minQuantity);
    	}
		
		$('.input-field-quantity').val(newQuantity );
	});
	
	$('.button2quantity').on('click', function(){
		var quantity = $('.input-field-quantity').val()
		var maxQuantity = parseInt($('.input-field-quantity').attr("max"));
		var newQuantity = parseInt(quantity) + 1;
		
		
		if (newQuantity > maxQuantity) {
       		 newQuantity = maxQuantity;
    	}
		
		$('.input-field-quantity').val(newQuantity );
	});
	
	
	$('#formId #buttonSubbmit').on('click', function(event) {
		event.preventDefault();
		$.get("/shop/checkAuthentication", function(checkAuthentication) {
				if(checkAuthentication!= null && checkAuthentication === 'no'){
					window.location.href="/shop/login";
					return;
				}
			});
		

		$('#exampleModal').modal('show');

		setTimeout(function() {
			$('#exampleModal').modal('hide');
		}, 2000);

		$('#exampleModal').on('shown.bs.modal', function(e) {
			// Gọi hàm chỉ khi modal đã được ẩn
			$.get("/shop/getTotalItems", function(totalItem) {
				console.log(totalItem)
				if (totalItem != null && totalItem !== undefined) {
					$('.badge').text(totalItem);
				}

			});
		});
		
		$('#formId').submit(); 

		var maxQuantity2 = parseInt($('.input-field-quantity').attr("max"));
		var quantity2 = parseInt($('.input-field-quantity').val());
		var newMaxQuantity2 = maxQuantity2 - quantity2;
		var minQuantity2 = parseInt($('.input-field-quantity').attr("min"));
		
		if(quantity2 > newMaxQuantity2){
			$('.input-field-quantity').val(newMaxQuantity2);
		}
		
		$('.input-field-quantity').attr("max", newMaxQuantity2);
		if(newMaxQuantity2 == 0){
			$('.input-field-quantity').attr("min", 0);
		}	
	});
	
	$('#imageButton').on('click', function(event) {
		event.preventDefault();

		var href = $(this).attr('href');
		$('#imageButtonModal').attr('src', href);

		$('#imageModal').modal('show');
	});


});