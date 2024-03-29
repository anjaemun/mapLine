package kr.co.icia.mapline.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.databind.ObjectMapper;

import kr.co.icia.mapline.util.KakaoApiUtil;
import kr.co.icia.mapline.util.KakaoApiUtil.Point;

@Controller
public class MapController {

	/**
	 * 자동차 이동 경로 그리기
	 * 
	 * @param fromAddress 출발지 주소정보
	 * @param toAddress   목적지 주소정보
	 * @param model       html파일에 값을 전달해주는 객체
	 * @return html 파일위치
	 * 
	 */
	@GetMapping("/map/paths") // url : /map/paths
	public String getMapPaths(@RequestParam(required = false) String fromAddress, //
			@RequestParam(required = false) String toAddress, //
			Model model) throws IOException, InterruptedException {
		Point fromPoint = null;
		Point toPoint = null;
		if (fromAddress != null && !fromAddress.isEmpty()) {
			fromPoint = KakaoApiUtil.getPointByAddress(fromAddress);
			model.addAttribute("fromPoint", fromPoint);
		}
		if (toAddress != null && !toAddress.isEmpty()) {
			toPoint = KakaoApiUtil.getPointByAddress(toAddress);
			model.addAttribute("toPoint", toPoint);
		}

		if (fromPoint != null && toPoint != null) {
			List<Point> pointList = KakaoApiUtil.getVehiclePaths(fromPoint, toPoint);
			String pointListJson = new ObjectMapper().writer().writeValueAsString(pointList);
			model.addAttribute("pointList", pointListJson);
		}
		return "map/paths";
	}

	/**
	 * 주소를 좌표로 변환
	 * 
	 * @param address 주소정보
	 * @param model   html파일에 값을 전달해주는 객체
	 * @return html 파일위치
	 * 
	 */
	@GetMapping("/map/address/point") // url : /map/address/point
	public String getMapAddressPoint(@RequestParam(required = false) String address, Model model)
			throws IOException, InterruptedException {
		if (address != null && !address.isEmpty()) {
			Point point = KakaoApiUtil.getPointByAddress(address);
			model.addAttribute("point", point);
		}
		return "map/address_point";
	}

	/**
	 * 출발지와 목적지를 지도상에 표시하기
	 * 
	 * @param fromAddress 출발지 주소정보
	 * @param toAddress   목적지 주소정보
	 * @param model       html파일에 값을 전달해주는 객체
	 * @return html 파일위치
	 * 
	 */
	@GetMapping("/map/marker") // url : /map/marker
	public String getMapMarker(@RequestParam(required = false) String fromAddress, //
			@RequestParam(required = false) String toAddress, //
			Model model) throws IOException, InterruptedException {
		if (fromAddress != null && !fromAddress.isEmpty()) {
			Point fromPoint = KakaoApiUtil.getPointByAddress(fromAddress);
			model.addAttribute("fromPoint", fromPoint);
		}
		if (toAddress != null && !toAddress.isEmpty()) {
			Point toPoint = KakaoApiUtil.getPointByAddress(toAddress);
			model.addAttribute("toPoint", toPoint);
		}
		return "map/marker";
	}
	/**
     * 키워드로 검색하기
     *
     * @param keyword 검색어
     * @param x       중심좌표 x
     * @param y       중심좌표 y
     * @param model   html파일에 값을 전달해주는 객체
     * @return html 파일위치
     */
    @GetMapping("/map/keyword")
    public String getKeyword(@RequestParam(required = false) String keyword, //keyword를 입력받음
                             @RequestParam(required = false) String x, //x좌표를 입력받음
                             @RequestParam(required = false) String y, Model model) throws IOException, InterruptedException { //y좌표를 입력받음
        if (keyword != null && !keyword.isEmpty() &&
            x != null && !x.isEmpty() &&
            y != null && !y.isEmpty()) { //keyword, x, y값이 모두 입력되었을 때 실행
            List<KakaoApiUtil.Pharmacy> pharmacyList = KakaoApiUtil.getPointsByKeyword(keyword, x, y); //keyword, x, y값을 getPointsByKeyword에 넣어서 반환되는 Pharmacy로 구성된 List를 저장
            int cnt = 0; //pharmacyList의 크기를 저장할 변수
            assert pharmacyList != null;//pharmacyList가 null이 아닐 때 실행
            for (KakaoApiUtil.Pharmacy pharmacy : pharmacyList) { //pharmacyList의 크기만큼 반복
                cnt++; //pharmacyList의 크기를 저장
            }
            System.out.println(cnt); //pharmacyList의 크기를 출력
            String pharmacyListJson = new ObjectMapper().writer().writeValueAsString(pharmacyList); //pharmacyList를 json형태로 변환
            model.addAttribute("pharmacyList", pharmacyListJson); //html로 보냄
            System.out.println("실행됨"); //실행됐는지 확인
        }
        return "map/keyword"; //html 파일위치
    }



}