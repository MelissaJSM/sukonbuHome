package com.live2d.demo.full;

public class S_FinalData {


    /**
     * TODO [클래스 설명]
     * // -----------------------------------------
     * 1. 상수 데이터 관리 클래스
     * // -----------------------------------------
     * */





    // TODO [모바일 웹뷰 주소 로드 정의 실시]
    public static final String WV_LOAD_MAIN_ENZ_URL = "https://www.naver.com"; // [메인 : 운영 주소]
    public static final String WV_LOAD_MAIN_DEV_URL = "https://www.google.com"; // [메인 : 개발 주소]

    public static final String WV_LOAD_NAVER_URL = "https://www.naver.com"; // [내부 링크 : 네이버]





    // TODO [웹뷰 모바일 뒤로가기 종료 위한 주소]
    public static final String WV_BACK_URL_SCHOOL = "HCO1234"; // [학교 선택 웹뷰 경로]
    public static final String WV_BACK_URL_LOGIN = "HCO5678"; // [로그인 웹뷰 경로]
    public static final String WV_BACK_URL_MAIN = "HCO9012"; // [메인 웹뷰 경로]





    // TODO [외부 링크 이동 주소 정의 실시]
    public static final String LINK_GO_NAVER = "https://www.naver.com"; // [외부 링크 : 네이버]





    // TODO [알림창 메시지 선언]
    public static final String AL_TITLE = "알림";
    public static final String AL_SET = "설정";
    public static final String AL_OK = "확인";
    public static final String AL_COPY = "복사";
    public static final String AL_NO = "취소";

    public static final String AL_NET = "현재 연결된 네트워크를 다시 확인해주세요.";
    public static final String AL_NLP = "위치 권한 사용을 허용해주세요.";
    public static final String AL_BEC = "비콘 스캔 기능 확인이 필요합니다.";
    public static final String AL_NBD = "블루투스를 지원하지 않는 기기입니다.";
    public static final String AL_NFD = "NFC를 지원하지 않는 기기입니다.";
    public static final String AL_NBP = "블루투스 권한 사용을 허용해주세요.";
    public static final String AL_NPA = "권한 허가를 동의 하지않으셨습니다."+"\n"
            +"일부 기능 사용에 제한이 있을 수 있습니다"+"\n"
            +"[설정] > [권한]에서 거부한 권한을 활성해주세요";

    public static final String AL_NQA = "안전한 앱 사용을 위해 보안 상태 확인 권한 동의를 해주세요.";
    public static final String AL_NFA = "악성 앱 설치 여부를 확인하기 위해 파일 접근 권한을 동의해주세요.";

    public static final String AL_NBE = "블루투스 비활성 상태입니다. 블루투스를 활성화해주세요.";
    public static final String AL_NLE = "위치 사용이 비활성 상태입니다. 위치 사용을 활성화해주세요.";
    public static final String AL_NFE = "NFC 비활성 상태입니다. [NFC 기본 모드] 를 활성화해주세요.";

    public static final String AL_SND = "흔들기 이벤트를 사용할 수 없는 디바이스입니다.";





    // TODO [브로드 캐스트 알림 채널]
    public static final String NOTI_RECEIVE_PUSH_MESSAGE = "NOTI_RECEIVE_PUSH_MESSAGE"; // [채널 : 파이어베이스 푸시 알림 받음]
    public static final String NOTI_RECEIVE_CUSTOM_ALERT = "NOTI_RECEIVE_CUSTOM_ALERT"; // [채널 : 커스텀 팝업창 호출 알림 받음]





    // TODO [외부 앱 메인 이동 수행 패키지명 등록]
    public static final String APP_GO_CHROME = "com.android.chrome"; // [크롬 외부 앱 이동]
    public static final String APP_GO_KAKAO = "com.kakao.talk"; // [카카오톡 외부 앱 이동]
    public static final String APP_GO_TEST = "com.test.app"; // [테스트 앱 외부 앱 이동]





    // TODO [스키마 외부앱 이동 수행 등록]
    public static final String SCHEME_GO_TEST = "test://call?name=twok&age=29"; // [테스트 앱 스키마 이동]





    // TODO [스키마 접속 확인 위한 스키마, 호스트 : AndroidManifest.xml 등록 필요]
    public static final String SCHEME_IN_TITLE_APP = "test"; // [앱 : 스키마]

    public static final String SCHEME_IN_HOST_LOGIN = "login"; // [앱 : 호스트 : 로그인 연동]
    public static final String SCHEME_IN_HOST_CALL = "call"; // [앱 : 호스트 : 일반 호출]





    // TODO [에러 메시지 관련 처리]
    public static final String ERROR_URL_HTTP = "주소 형식을 다시 확인해 주세요. [http, https is not found]";
    public static final String ERROR_DATA_NULL = "데이터 확인 중 문제가 발생했습니다. [data type error]";
    public static final String ERROR_PARSING_KEY = "데이터 확인 중 문제가 발생했습니다. [parsing [key] is not found]";
    public static final String ERROR_PARSING_DATA = "데이터 확인 중 문제가 발생했습니다. [parsing [data] is null]";
    public static final String ERROR_JSON_PARSING = "데이터 확인 중 문제가 발생했습니다. [json type error]";
    public static final String ERROR_WEBVIEW_LOAD = "웹 로드 중 문제가 발생했습니다. [WebView load Error]";





    // TODO [프리퍼런스 데이터 저장 KEY 정의 / 하드 코딩 데이터 정의]
    public static final String PRE_USER_UNIV = "PRE_USER_UNIV"; // [학교 구분]
    public static final String PRE_USER_UID = "PRE_USER_UID"; // [사용자 학번 및 직번]
    public static final String PRE_USER_UPW = "PRE_USER_UPW"; // [사용자 비밀번호]
    public static final String PRE_USER_IDDI = "PRE_USER_IDDI"; // [사용자 신분]
    public static final String PRE_USER_PICTURE = "PRE_USER_PICTURE"; // [사용자 사진 표시]
    public static final String PRE_AUTO_LOGIN = "PRE_AUTO_LOGIN"; // [자동 로그인 구분]

    public static final String PRE_MOBILE_SHAKE = "PRE_MOBILE_SHAKE"; // [흔들기 이벤트 발생 여부]
    public static final String PRE_MOBILE_MOCO = "PRE_MOBILE_MOCO"; // [디바이스 고유값]
    public static final String PRE_MOBILE_VERSION = "PRE_MOBILE_VERSION"; // [모바일 버전 명칭 : 1.0.0]
    public static final String PRE_MOBILE_CODE = "PRE_MOBILE_CODE"; // [모바일 버전 코드 : 1]
    public static final String PRE_MOBILE_PLAT = "android"; // [하드 코딩 : 모바일 플랫 폼]

    public static final String PRE_PUSH_TOKEN = "PRE_PUSH_TOKEN"; // [푸시 토큰 값 : 파이어베이스]

    public static final String PRE_SCHEME_DATA_LOGIN = "PRE_SCHEME_DATA_LOGIN"; // [스키마 데이터 저장 : 로그인 : json 형식]
    public static final String PRE_SCHEME_DATA_CALL = "PRE_SCHEME_DATA_CALL"; // [스키마 데이터 저장 : 일반 : json 형식]

    public static final String PRE_PUSH_SORT = "PRE_PUSH_SORT"; // [푸시 알림 종류 : 파이어베이스]

    public static final String PRE_APP_NEW_TASK = "PRE_APP_NEW_TASK"; // [스키마 및 일반 애플리케이션 실행 여부 : A_Intro]
    public static final String PRE_ROOT_ACTIVITY = "PRE_ROOT_ACTIVITY"; // [메인 웹뷰 실행 여부 : 포그라운드에서 푸시 알림 발생 시 JS 전달 위함]

    public static final String PRE_WV_RESUME_TIME = "PRE_WV_RESUME_TIME"; // [메인 웹뷰 포그라운드 접속 시간 : A_Main]
    public static final String PRE_WV_PAUSE_TIME = "PRE_WV_PAUSE_TIME"; // [메인 웹뷰 백그라운드 접속 시간 : A_Main]

    public static final String PRE_WV_SAVE_TIME = "30"; // [하드 코딩 : 분 단위 : 앱 백그라운드 >> 포그라운드 전환 시 초기화 시간]
    public static final String PRE_APP_UPDT_TIME = "PRE_APP_UPDT_TIME"; // [앱 라이프 사이클 갱신 시간 [인트로 재수행] : SAVE TIME 저장 시간]

    public static final String PRE_CAMERA_SCAN = "PRE_CAMERA_SCAN"; // [카메라 스캔 결과]
    public static final String PRE_CAMERA_SETTINGS = "PRE_CAMERA_SETTINGS"; // [카메라 전방 , 후방]

    public static final String PRE_WV_COOKIE_DATA = "PRE_WV_COOKIE_DATA"; // [쿠키 설정 및 웹뷰 로드 위한 데이터 저장 값]





    // TODO [공통 데이터 파싱 KEY 명칭 정의 실시]
    public static final String COMMON_NAME_KEY = "key";
    public static final String COMMON_NAME_VALUE = "value";





    // TODO [공통 데이터 리턴 문자열 정의 실시]
    public static final String RETURN_INSERT_SUCCESS_STRING = "Y : INSERT";
    public static final String RETURN_INSERT_FAIL_STRING = "N : INSERT";

    public static final String RETURN_DELETE_SUCCESS_STRING = "Y : DELETE";
    public static final String RETURN_DELETE_FAIL_STRING = "N : DELETE";


} // TODO [클래스 종료]