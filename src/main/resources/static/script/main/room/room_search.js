/**
 * 
 */

function searchWordReset() { 
	document.querySelector("input[name='searchWord']").value = "";
}

$(document).ready(function () {
	//위치 기본값(강남 이젠)
	let centerPosition = new kakao.maps.LatLng(37.5026685, 127.0221589);
	
	if (navigator.geolocation) {
	    
	    // GeoLocation을 이용해서 접속 위치를 얻어옵니다
	    navigator.geolocation.getCurrentPosition( function(position) {
	        let lat = position.coords.latitude; // 위도
	        let lon = position.coords.longitude; // 경도
	        
	        centerPosition = new kakao.maps.LatLng(lat, lon);
	    });
	}
	
	let addressArray=[];
	
	console.log($(".address"));
	
	$.each($(".address"), function(index, element){
	    addressArray.push({
	    	"roomId" : element.attributes.data.value ,
	    	"address" : element.value
    	});
	});
	console.log(addressArray);

	var container = document.getElementById('map'); //지도를 담을 영역의 DOM 레퍼런스
    var options = { //지도를 생성할 때 필요한 기본 옵션
   		center: centerPosition,
        level: 3 //지도의 레벨(확대, 축소 정도)
    };

    var map = new kakao.maps.Map(container, options); //지도 생성 및 객체 리턴

    
    var geocoder = new kakao.maps.services.Geocoder();

    //바운즈 - 마커 중앙설정
    bounds = new kakao.maps.LatLngBounds();
    
    $.each(addressArray, function(index, item) {
    	
    	console.log(item.address);
    	
		// 주소로 좌표를 검색합니다
		geocoder.addressSearch(item.address, function(result, status) {
		    // 정상적으로 검색이 완료됐으면 
		    if (status === kakao.maps.services.Status.OK) {
				var coords = new kakao.maps.LatLng(result[0].y, result[0].x);
				
				bounds.extend(coords);
				 
				// 결과값으로 받은 위치를 마커로 표시합니다
				var marker = new kakao.maps.Marker({
				    map: map,
				    clickable: true,
				    position: coords
				});
				
				//마커 클릭시 좌측 매물 리스트에서 클릭한 매물 하나만 표출
				kakao.maps.event.addListener(marker, 'click', function() {
					map.setCenter(coords);
					map.setLevel(3);
					roomItemShowController(item.roomId);
				});
		    }
    		map.setBounds(bounds);
		});
		
    });
    
    //맵 클릭시 매물 리스트 전체 표시
    kakao.maps.event.addListener(map, 'click', function() {
    	$(".room-item").show();
		map.setBounds(bounds);
    });
    
    //매물 하나만 표시
	function roomItemShowController(roomId) {
		$(".room-item").hide();
		$(document.querySelector(".room-"+roomId)).show();
	}


});