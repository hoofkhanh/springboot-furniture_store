$(document).ready(function() {


	$('.quantityClass').on('input', function() {
		/*truy cập vào class cha gần nhất (closest)*/
		var form = $(this).closest('.input-button-container').closest('.formClass');
		var id = form.find('.productClass').val();
		var quantity = $(this).val();
		var maxQuantity = $(this).attr("max");
		
		if(quantity.indexOf('.') > -1){
			var newValue = parseInt(quantity*10);
			console.log(newValue)
			quantity= newValue
			$(this).val(newValue)
		}


		if (quantity != '' && parseInt(quantity) > parseInt(maxQuantity)) {
			var nameWarning = form.closest('.quantity-box').closest('.tr_each').find('.name-pr').find('.nameClass').text();
			quantity = maxQuantity;
			$(this).val(maxQuantity);
			$('#warningText').text('Bạn chỉ có thể mua sản phẩm "' + nameWarning + '" với số lượng tối đa là ' + maxQuantity + ' sản phẩm');
			$('#warningModal').modal('show');

			setTimeout(function() {
				$('#warningModal').modal('hide');
			}, 2000);


		}

		/*var deleteProduct = true;
*/		if (parseInt(quantity) <= 0) {

			/*var nameDelete = form.closest('.quantity-box').closest('.tr_each').find('.name-pr').find('.nameClass').text();
			$('#deleteP').text('Bạn có muốn xóa sản phẩm "' + nameDelete + '" không ?');
			$('#deleteModal').modal('show');

			$('#closeButton').on('click', function() {
				$('#deleteModal').modal('hide');
			});

			$('#confirmDelete').on('click', function(event) {
				event.stopPropagation();
				var hrefdelete = form.closest('.quantity-box').closest('.tr_each').find('.remove-pr').find('.formDeleteClass').attr('action');
				var idProductDelete = form.closest('.quantity-box').closest('.tr_each').find('.remove-pr').find('.formDeleteClass').find('.productClassDelete').val();
				var rowRemove = form.closest('.quantity-box').closest('.tr_each');
				$.ajax({
					url: hrefdelete,
					type: 'GET',
					data: { id: idProductDelete },
					success: function() {
						$(rowRemove).remove();
						$.get("/shop/getTotalItems", function(totalItem) {
							if (totalItem != null && totalItem !== undefined) {
								$('.badge').text(totalItem);
							}
						});
						$('#deleteModal').modal('hide');
						deleteProduct = false;
					},
					error: function(error) {
						console.error('Error deleting object: ', error);
						console.log(error.responseText);
					}
				});
				
			});*/

			quantity = 1;
			$(this).val(1)

			/*return;*/
		}

		/*truy cập vào 2 class cha gần nhất bằng (closest) và vào lớp 2 lớp con bằng find*/
		var totalPrice = form.closest('.quantity-box').closest('.tr_each').find('.total-pr').find('.totalPriceClass');


		if (quantity != '') {
			$.ajax({
				type: 'GET',
				url: '/shop/cart/update?id=' + id + '&quantity=' + quantity,
				success: function(response) {
					var formatTotalPrice = numeral(response).format('0,0.0');
					totalPrice.text('$' + formatTotalPrice);


					$.ajax({
						type: "GET",
						url: "/shop/checkTotalPirce",
						success: function(totalPrice) {
							totalPrice = parseFloat(totalPrice);
							if (totalPrice != null) {
								totalPrice = parseFloat(totalPrice);
								
								var formatTotalPrice = numeral(totalPrice).format('0,0.0');
								console.log(formatTotalPrice)
								$('#totalPriceId').text('$' + formatTotalPrice);
									

								var newTotal = numeral(totalPrice + 30).format('0,0.0');
								console.log(newTotal)
								$('#totalId').text('$' + newTotal);

							}
						},
						error: function() {
							console.error('Error updating item in cart');
						}
					});
				},
				error: function() {
					console.error('Error updating item in cart');
				}
			});
		}

	});


	$('.formDeleteClass #buttonDelete').on('click', function(event) {
		event.preventDefault();
		var form = $(this).closest('.formDeleteClass');
		var idProduct = form.find('.productClassDelete').val();
		console.log(idProduct);
		var hrefDelete = $(this).closest('.formDeleteClass').attr('action');
		var rowRemove = $(this).closest('.formDeleteClass').closest('.remove-pr').closest('.tr_each');
		$.ajax({
			url: hrefDelete,
			type: 'GET',
			data: { id: idProduct },
			success: function() {
				$(rowRemove).remove();
				$.get("/shop/getTotalItems", function(totalItem) {
					if (totalItem != null && totalItem !== undefined) {
						$('.badge').text(totalItem);
					}
				});

				$.ajax({
						type: "GET",
						url: "/shop/checkTotalPirce",
						success: function(totalPrice) {
							totalPrice = parseFloat(totalPrice);
							if (totalPrice != null) {
								totalPrice = parseFloat(totalPrice);
								
								var formatTotalPrice = numeral(totalPrice).format('0,0.0');
								console.log(formatTotalPrice)
								$('#totalPriceId').text('$' + formatTotalPrice);
									

								var newTotal = numeral(totalPrice + 30).format('0,0.0');
								console.log(newTotal)
								$('#totalId').text('$' + newTotal);

							}
						},
					error: function() {
						console.error('Error updating item in cart');
					}
				});

				$.ajax({
					type: "GET",
					url: "/shop/checkEmptyCartItems",
					success: function(text) {
						if (text == 'empty') {
							var checkoutId = document.getElementById("checkoutId");
							checkoutId.style.display = "none";

							document.getElementById("tableId").style.display = "none";

							document.getElementById('checkkDiv').style.display = 'block';
						}
					},
					error: function() {
						console.error('Error');
					}
				});
			},
			error: function(error) {
				console.error('Error deleting object: ', error);
			}
		});


	});


	$('.button1quantity').on('click', function() {
		var quantity = $(this).closest('.input-button-container').find('.quantityClass').val();
		var newQuantity = quantity - 1;

		if (newQuantity <= 0) {
			newQuantity = 1;
		}

		$(this).closest('.input-button-container').find('.quantityClass').val(newQuantity);

		$(this).closest('.input-button-container').find('.quantityClass').trigger('input');
	});

	$('.button2quantity').on('click', function() {
		var quantity = $(this).closest('.input-button-container').find('.quantityClass').val();
		var maxQuantity = parseInt($(this).closest('.input-button-container').find('.quantityClass').attr("max"));
		var newQuantity = parseInt(quantity) + 1;


		if (newQuantity > maxQuantity) {
			newQuantity = maxQuantity;
		}

		$(this).closest('.input-button-container').find('.quantityClass').val(newQuantity);

		$(this).closest('.input-button-container').find('.quantityClass').trigger('input');
	});
	



});