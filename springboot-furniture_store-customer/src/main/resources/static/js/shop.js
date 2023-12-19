$(document).ready(function() {
	
	

	var productList = document.querySelectorAll('#productList');
	var loadMore = document.getElementById('loadMore');
	loadMore.style.display='block';
	
	var index = 0;
	var maxIndex =6;
	

	if(maxIndex > productList.length){
			maxIndex = productList.length;
	}
	
	for(var i = index; i< maxIndex; i++){
		productList[i].style.display='block';
	}
	
	
	if(maxIndex >= productList.length){
		loadMore.style.display = 'none';
	}

	
	$('#loadMore').on('click', function(){
		if(maxIndex<= productList.length &&  maxIndex +6 > productList.length){
			index= maxIndex;
			maxIndex = productList.length;
		}else{
			index=maxIndex;
			maxIndex += 6
		}
				
		
		for(var i = index; i< maxIndex ; i++){
			productList[i].style.display='block';						
		}
		
		if(maxIndex >= productList.length){
			loadMore.style.display = 'none';
		}
		
	})
	
	

	var productList2 = document.querySelectorAll('#productList2');
	var loadMore2 = document.getElementById('loadMore2');
	loadMore2.style.display='block';
	
	var index2 = 0;
	var maxIndex2 =3;
	

	if(maxIndex2 > productList2.length){
			maxIndex2 = productList2.length;
	}
	
	for(var i = index2; i< maxIndex2; i++){
		productList2[i].style.display='block';
	}
	
	if(maxIndex2 >= productList2.length){
		loadMore2.style.display = 'none';
	}
	
	console.log(index2)
	console.log(maxIndex2)

	
	$('#loadMore2').on('click', function(){
		if(maxIndex2<= productList2.length &&  maxIndex2 +3 > productList2.length){
			index2= maxIndex2;
			maxIndex2 = productList2.length;
		}else{
			index2=maxIndex2;
			maxIndex2 += 3;
		}
			console.log(index2)
	
		
		for(var i = index2; i< maxIndex2 ; i++){
			productList2[i].style.display='block';						
		}
		
		if(maxIndex2 >= productList2.length){
			loadMore2.style.display = 'none';
		}
		
	});
	
	
	
	

	
});