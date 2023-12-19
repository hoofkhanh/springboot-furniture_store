$(document).ready(function(){
	
	$('#tr_each  #deleteButton').on('click', function(event) {
		event.preventDefault();
		var idOrder = $(this).closest('.trOrderClass').find('.orderClass').val();
		var hrefDelete = $(this).attr('href');
		
		var removeRow = $('.tr_each' + idOrder);

		$.ajax({
			url: hrefDelete,
			type: 'GET',
			data: { id: idOrder },
			success: function() {
				removeRow.remove();
				
				$.ajax({
					url: '/shop/checkSizeOrder',
					type: 'GET',
					data: {},
					success: function(text) {
						if(text == 'null'){
										
							document.getElementById("tableId").style.display ="none";
										
							document.getElementById('checkkDiv').style.display= 'block'; 
						}
					},
					error: function(error) {
						console.error('Error deleting object: ', error);
					}
				});
			},
			error: function(error) {
				console.error('Error deleting object: ', error);
			}
		});
			
			
	});
	
	$('#imageButton').on('click', function(event) {
		event.preventDefault();

		var href = $(this).attr('href');
		console.log(href)
		$('#imageButtonModal').attr('src', href);

		$('#imageModal').modal('show');
		
		
	});
});