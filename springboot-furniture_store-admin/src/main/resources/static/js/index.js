$(document).ready(function() {
	
	$.get('/admin/findAllOrder',function(orderList){
		var data = {
		labels: ['Tháng 1', 'Tháng 2', 'Tháng 3', 'Tháng 4', 'Tháng 5', 'Tháng 6','Tháng 7','Tháng 8','Tháng 9','Tháng 10', 'Tháng 11', 'Tháng 12', ],
		datasets: [{
			label: 'Doanh số bán hàng (tính bằng $)',
			data: [],
			backgroundColor: 'rgba(255, 165, 0, 0.5)', // Màu nền
			borderColor: 'rgba(75, 192, 192, 1)', // Màu đường viền
			borderWidth: 1 // Độ rộng đường viền
		}]
		};
		
		for (var i = 0; i < orderList.length; i++) {
	        var monthIndex = orderList[i][0];
	        var total = orderList[i][1];
	        
	      
	        data.datasets[0].data[monthIndex - 1] = total;
        }
        
        // Lấy tham chiếu đến phần tử canvas
	var ctx = document.getElementById('myChart').getContext('2d');

	// Tạo biểu đồ
	var myChart = new Chart(ctx, {
		type: 'bar', // Loại biểu đồ (có thể thay đổi thành 'line', 'pie',...)
		data: data, // Dữ liệu đã chuẩn bị
		options: {
			scales: {
				y: {
					beginAtZero: true
				}
			}
		}
	});
	});
	
	

	
});
