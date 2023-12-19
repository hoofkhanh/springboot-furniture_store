
$('document').ready(function() {

	$('.table #editButton').on('click', function(event) {
		event.preventDefault();

		var href = $(this).attr('href');

		$.get(href, function(product, status) {
			$('#idEdit').val(product.id);
			$('#nameEdit').val(product.name);

			if (product.category != null) {
				$('#categoryEdit').val(product.category.id);
			} else {
				$('#categoryEdit').val(product.category);
			}

			$('#descriptionEdit').val(product.description);
			$('#currentQuantityEdit').val(product.currentQuantity);
			$('#costPriceEdit').val(product.costPrice);
			$('#salesEdit').val(product.salePrice);
			$('#activatedEdit').val(product.activated);
			$('#hiddenEdit').val(product.hidden);
			$('#soldEdit').val(product.sold);

			if (product.image) {
				$('#imgEdit').show();
				$('#imgEdit').attr('src', 'data:image/jpeg;base64,' + product.image);
			} else {
				$('#imgEdit').hide();
			}

		});

		$('#editModal').modal('show');
	});

	$('.table #hiddenButton').on('click', function(event) {
		event.preventDefault();

		var href = $(this).attr('href');
		$('#confirmHiddenButton').attr('href', href);

		$('#hiddenModal').modal('show');
	});

	$('.table #activatedButton').on('click', function(event) {
		event.preventDefault();

		var href = $(this).attr('href');
		$('#confirmActivatedButton').attr('href', href);

		$('#activatedModal').modal('show');
	});
	
	$('.table #deleteButton').on('click', function(event) {
		event.preventDefault();

		var href = $(this).attr('href');
		$('#confirmDeleteButton').attr('href', href);

		$('#deleteModal').modal('show');
	});

	$('.table #imageButton').on('click', function(event) {
		event.preventDefault();

		var href = $(this).attr('href');
		$('#imageButtonModal').attr('src', href);

		$('#imageModal').modal('show');
	});
	
	
	var inputValue = $('#inputSearch').val();
	$('#inputSearch').val('').val(inputValue).focus();

	$('#inputSearch').on('input', function() {
		if($(this).val().length  >= 0 ){				
			$('#search').submit();
		};
	});
	
	

});