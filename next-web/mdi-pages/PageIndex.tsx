import React from "react";
import  {CM_1100__프로그램관리}  from './CM/CM_1100__프로그램관리';
import  {CM_1300__메뉴관리}  from './CM/CM_1300__메뉴관리';
import  {CM_1400__공통코드}  from './CM/CM_1400__공통코드';
import {CM_1200__테이블도메인관리} from "./CM/CM_1200__테이블도메인관리";
import {CM_1600__테이블시퀀스} from "./CM/CM_1600__테이블시퀀스";

import  {CM_1840__SA에러로그}  from './CM/CM_1840__SA에러로그';
import  {CM_1850__로그LIST}  from './CM/CM_1850__로그LIST';
import {CM_4100__회원관리} from "./CM/CM_4100__회원관리";
import {CM_7100__테이블관리} from "./CM/CM_7100__테이블관리";
import { CM_7200__컬럼상세 } from "./CM/CM_7200__컬럼상세";
import { CM_7300__SQL } from "./CM/CM_7300__SQL";
import { CM_6100__게시판관리 } from "./CM/CM_6100__게시판관리";
import { CM_6200__게시판 } from "./CM/CM_6200__게시판";
import { CM_4200__역할관리 } from "./CM/CM_4200__역할관리";
import { CM_1900__코인계좌정보 } from "./CM/CM_1900__코인계좌정보";

import { FX_0110__KRW업비트TO바이낸스 } from "./FX/FX_0110__KRW업비트TO바이낸스";
import { FX_0120__KRW업비트TO바이낸스호가 } from "./FX/FX_0120__KRW업비트TO바이낸스호가";
import { FX_0121__KRW업비트TO바이낸스호가REPORT } from "./FX/FX_0121__KRW업비트TO바이낸스호가REPORT";
import { FX_0125__출금코인 } from "./FX/FX_0125__출금코인";
import { FX_0130__재정거래업비트 } from "./FX/FX_0130__재정거래업비트";
import { FX_0135__업비트호가 } from "./FX/FX_0135__업비트호가";
import { FX_0140__재정거래바이낸스 } from "./FX/FX_0140__재정거래바이낸스";
import { FX_0145__바이낸스호가 } from "./FX/FX_0145__바이낸스호가";
import { FX_0160__업비트주문 } from "./FX/FX_0160__업비트주문";
import { FX_0170__업비트출금 } from "./FX/FX_0170__업비트출금";
import { FX_0180__바이낸스주문 } from "./FX/FX_0180__바이낸스주문";
import { FX_0190__바이낸스출금 } from "./FX/FX_0190__바이낸스출금";
import { UPBIT_0110__업비트종목관리 } from "./UPBIT/UPBIT_0110__업비트종목관리";
import { UPBIT_0120__업비트종목시세 } from "./UPBIT/UPBIT_0120__업비트종목시세";
import { UPBIT_0130__업비트종목분시세 } from "./UPBIT/UPBIT_0130__업비트종목분봉시세";
import { UPBIT_0140__업비트종목일별시세 } from "./UPBIT/UPBIT_0140__업비트종목일별시세";
import { UPBIT_0150__업비트종목주별시세 } from "./UPBIT/UPBIT_0150__업비트종목주별시세";
import { UPBIT_0160__업비트종목월별시세 } from "./UPBIT/UPBIT_0160__업비트종목월별시세";
import { UPBIT_0170__업비트체결조회 } from "./UPBIT/UPBIT_0170__업비트체결조회";
import { UPBIT_0180__업비트호가조회 } from "./UPBIT/UPBIT_0180__업비트호가조회";
import { UPBIT_0210__업비트전체계좌조회 } from "./UPBIT/UPBIT_0210__업비트전체계좌조회";
import { UPBIT_0225__업비트주문리스트조회 } from "./UPBIT/UPBIT_0225__업비트주문리스트조회";
import { UPBIT_0235__업비트출금리스트 } from "./UPBIT/UPBIT_0235__업비트출금리스트";
import { UPBIT_0250__업비트입금리스트조회 } from "./UPBIT/UPBIT_0250__업비트입금리스트조회";
import { UPBIT_0260__업비트전체입금주소조회 } from "./UPBIT/UPBIT_0260__업비트전체입금주소조회";
import { UPBIT_0270__업비트입출금현황 } from "./UPBIT/UPBIT_0270__업비트입출금현황";
import { UPBIT_0275__업비트API키리스트조회 } from "./UPBIT/UPBIT_0275__업비트API키리스트조회";
import { BINANCE_0110__모든코인정보_USER_DATA } from "./BINANCE/BINANCE_0100/BINANCE_0110__모든코인정보_USER_DATA";
import { BINANCE_0115__시스템상태 } from "./BINANCE/BINANCE_0100/BINANCE_0115__시스템상태";
import { BINANCE_0120__일일계정현황현물 } from "./BINANCE/BINANCE_0100/BINANCE_0120__일일계정현황현물";
import { BINANCE_0125__일일계정현황마진 } from "./BINANCE/BINANCE_0100/BINANCE_0125__일일계정현황마진";
import { BINANCE_0130__일일계정현황선물 } from "./BINANCE/BINANCE_0100/BINANCE_0130__일일계정현황선물";
import { BINANCE_0135__빠른인출스위치 } from "./BINANCE/BINANCE_0100/BINANCE_0135__빠른인출스위치";
import { BINANCE_0140__출금 } from "./BINANCE/BINANCE_0100/BINANCE_0140__출금";
import { BINANCE_0145__입금내역 } from "./BINANCE/BINANCE_0100/BINANCE_0145__입금내역";
import { BINANCE_0150__출금내역 } from "./BINANCE/BINANCE_0100/BINANCE_0150__출금내역";
import { BINANCE_0155__입금주소 } from "./BINANCE/BINANCE_0100/BINANCE_0155__입금주소";
import { BINANCE_0160__계좌상태 } from "./BINANCE/BINANCE_0100/BINANCE_0160__계좌상태";
import { BINANCE_0165__계좌API트레이딩상태 } from "./BINANCE/BINANCE_0100/BINANCE_0165__계좌API트레이딩상태";
import { BINANCE_0170__DUST_LOG } from "./BINANCE/BINANCE_0100/BINANCE_0170__DUST_LOG";
import { BINANCE_0175__DUST전송 } from "./BINANCE/BINANCE_0100/BINANCE_0175__DUST전송";
import { BINANCE_0177__자산배당기록 } from "./BINANCE/BINANCE_0100/BINANCE_0177__자산배당기록";
import { BINANCE_0178__자산상세 } from "./BINANCE/BINANCE_0100/BINANCE_0178__자산상세";
import { BINANCE_0179__거래수수료 } from "./BINANCE/BINANCE_0100/BINANCE_0179__거래수수료";
import { BINANCE_0185__사용자범용전송 } from "./BINANCE/BINANCE_0100/BINANCE_0185__사용자범용전송";
import { BINANCE_0186__사용자범용전송내역 } from "./BINANCE/BINANCE_0100/BINANCE_0186__사용자범용전송내역";
import { BINANCE_0190__자금지갑 } from "./BINANCE/BINANCE_0100/BINANCE_0190__자금지갑";
import { BINANCE_0195__API키권한 } from "./BINANCE/BINANCE_0100/BINANCE_0195__API키권한";
import { BINANCE_0210__테스트연결 } from "./BINANCE/BINANCE_0200/BINANCE_0210__테스트연결";
import { BINANCE_0215__교환정보 } from "./BINANCE/BINANCE_0200/BINANCE_0215__교환정보";
import { BINANCE_0220__주문대장 } from "./BINANCE/BINANCE_0200/BINANCE_0220__주문대장";
import { BINANCE_0225__최근거래목록 } from "./BINANCE/BINANCE_0200/BINANCE_0225__최근거래목록";
import { BINANCE_0230__이전거래조회 } from "./BINANCE/BINANCE_0200/BINANCE_0230__이전거래조회";
import { BINANCE_0235__24시간코인변동가격 } from "./BINANCE/BINANCE_0200/BINANCE_0235__24시간코인변동가격";
import { BINANCE_0240__집계거래목록 } from "./BINANCE/BINANCE_0200/BINANCE_0240__집계거래목록";
import { BINANCE_0245__캔들데이터 } from "./BINANCE/BINANCE_0200/BINANCE_0245__캔들데이터";
import { BINANCE_0250__평균가격 } from "./BINANCE/BINANCE_0200/BINANCE_0250__평균가격";
import { BINANCE_0255__코인가격 } from "./BINANCE/BINANCE_0200/BINANCE_0255__코인가격";
import { BINANCE_0260__베스트주문 } from "./BINANCE/BINANCE_0200/BINANCE_0260__베스트주문";
import { BINANCE_0310__새로운주문테스트TRADE } from "./BINANCE/BINANCE_0300/BINANCE_0310__새로운주문테스트TRADE";
import { BINANCE_0315__새로운주문TRADE } from "./BINANCE/BINANCE_0300/BINANCE_0315__새로운주문TRADE";
import { BINANCE_0320__주문취소_TRADE } from "./BINANCE/BINANCE_0300/BINANCE_0320__주문취소_TRADE";
import { BINANCE_0325__미체결주문취소TRADE } from "./BINANCE/BINANCE_0300/BINANCE_0325__미체결주문취소TRADE";
import { BINANCE_0330__주문상태USER_DATA } from "./BINANCE/BINANCE_0300/BINANCE_0330__주문상태USER_DATA";
import { BINANCE_0340__모든주문USER_DATA } from "./BINANCE/BINANCE_0300/BINANCE_0340__모든주문USER_DATA";
import { BINANCE_0345__OCO신규주문TRADE } from "./BINANCE/BINANCE_0300/BINANCE_0345__OCO신규주문TRADE";
import { BINANCE_0335__현재미체결주문USER_DATA } from "./BINANCE/BINANCE_0300/BINANCE_0335__현재미체결주문USER_DATA";
import { BINANCE_0350__OCO취소TRADE } from "./BINANCE/BINANCE_0300/BINANCE_0350__OCO취소TRADE";
import { BINANCE_0355__OCO주문내역USER_DATA } from "./BINANCE/BINANCE_0300/BINANCE_0355__OCO주문내역USER_DATA";
import { BINANCE_0360__OCO모든주문내역USER_DATA } from "./BINANCE/BINANCE_0300/BINANCE_0360__OCO모든주문내역USER_DATA";
import { BINANCE_0370__계정정보USER_DATA } from "./BINANCE/BINANCE_0300/BINANCE_0370__계정정보USER_DATA";
import { BINANCE_0375__계정거래목록USER_DATA } from "./BINANCE/BINANCE_0300/BINANCE_0375__계정거래목록USER_DATA";
import { BINANCE_0365__OCO미체결내역USER_DATA } from "./BINANCE/BINANCE_0300/BINANCE_0365__OCO미체결내역USER_DATA";


//https://www.storyblok.com/tp/react-dynamic-component-from-json
//https://www.youtube.com/watch?v=NMxMWOZC-Ec
export interface IConfig{ 
    component: string
    id: string
}

interface Props {
    [key: string] : ()=> JSX.Element;
}

const keysToComponentMap:Props = {
    CM_1100: CM_1100__프로그램관리,
    CM_1200: CM_1200__테이블도메인관리,
    CM_1300: CM_1300__메뉴관리,
    CM_1400: CM_1400__공통코드,
    CM_1600: CM_1600__테이블시퀀스,    
    CM_1840: CM_1840__SA에러로그,
    CM_1850: CM_1850__로그LIST,
    CM_1900: CM_1900__코인계좌정보,
    CM_4100: CM_4100__회원관리,
    CM_4200: CM_4200__역할관리,
    CM_6100: CM_6100__게시판관리,
    CM_6200: CM_6200__게시판,    
    CM_7100: CM_7100__테이블관리,
    CM_7200: CM_7200__컬럼상세,
    CM_7300: CM_7300__SQL,


    FX_0110: FX_0110__KRW업비트TO바이낸스,
    FX_0120: FX_0120__KRW업비트TO바이낸스호가,
    FX_0121: FX_0121__KRW업비트TO바이낸스호가REPORT,
    FX_0125: FX_0125__출금코인,
    FX_0130: FX_0130__재정거래업비트,
    FX_0135: FX_0135__업비트호가,
    FX_0140: FX_0140__재정거래바이낸스,
    FX_0145: FX_0145__바이낸스호가,
    FX_0160: FX_0160__업비트주문,
    FX_0170: FX_0170__업비트출금,
    FX_0180: FX_0180__바이낸스주문,
    FX_0190: FX_0190__바이낸스출금,
    
    

    UPBIT_0110:UPBIT_0110__업비트종목관리,
    UPBIT_0120:UPBIT_0120__업비트종목시세,
    UPBIT_0130:UPBIT_0130__업비트종목분시세,
    UPBIT_0140:UPBIT_0140__업비트종목일별시세,
    UPBIT_0150:UPBIT_0150__업비트종목주별시세,
    UPBIT_0160:UPBIT_0160__업비트종목월별시세,
    UPBIT_0170:UPBIT_0170__업비트체결조회,
    UPBIT_0180:UPBIT_0180__업비트호가조회,

    UPBIT_0210:UPBIT_0210__업비트전체계좌조회,
    UPBIT_0225:UPBIT_0225__업비트주문리스트조회,
    UPBIT_0235:UPBIT_0235__업비트출금리스트,
    UPBIT_0250:UPBIT_0250__업비트입금리스트조회,
    UPBIT_0260:UPBIT_0260__업비트전체입금주소조회,
    UPBIT_0270:UPBIT_0270__업비트입출금현황,   
    UPBIT_0275:UPBIT_0275__업비트API키리스트조회,   


    BINANCE_0110 :BINANCE_0110__모든코인정보_USER_DATA,
    BINANCE_0115 :BINANCE_0115__시스템상태,
    BINANCE_0120 :BINANCE_0120__일일계정현황현물,
    BINANCE_0125 :BINANCE_0125__일일계정현황마진,
    BINANCE_0130 :BINANCE_0130__일일계정현황선물,
    BINANCE_0135 :BINANCE_0135__빠른인출스위치,
    BINANCE_0140 :BINANCE_0140__출금,
    BINANCE_0145 :BINANCE_0145__입금내역,
    BINANCE_0150 :BINANCE_0150__출금내역,
    BINANCE_0155 :BINANCE_0155__입금주소,
    BINANCE_0160 :BINANCE_0160__계좌상태,
    BINANCE_0165 :BINANCE_0165__계좌API트레이딩상태 ,
    BINANCE_0170 :BINANCE_0170__DUST_LOG ,
    BINANCE_0175 :BINANCE_0175__DUST전송 ,
    BINANCE_0177 :BINANCE_0177__자산배당기록 ,
    BINANCE_0178 :BINANCE_0178__자산상세 ,
    BINANCE_0179 :BINANCE_0179__거래수수료 ,
    BINANCE_0185 :BINANCE_0185__사용자범용전송 ,
    BINANCE_0186 :BINANCE_0186__사용자범용전송내역 ,
    BINANCE_0190 :BINANCE_0190__자금지갑 ,
    BINANCE_0195 :BINANCE_0195__API키권한 ,
    BINANCE_0210 :BINANCE_0210__테스트연결 ,
    BINANCE_0215 :BINANCE_0215__교환정보 ,
    BINANCE_0220 :BINANCE_0220__주문대장 ,
    BINANCE_0225 :BINANCE_0225__최근거래목록 ,
    BINANCE_0230 :BINANCE_0230__이전거래조회 ,
    BINANCE_0240 :BINANCE_0240__집계거래목록 ,
    BINANCE_0245 :BINANCE_0245__캔들데이터 ,
    BINANCE_0250 :BINANCE_0250__평균가격 ,
    BINANCE_0255 :BINANCE_0255__코인가격 ,
    BINANCE_0260 :BINANCE_0260__베스트주문 ,


    BINANCE_0310 :BINANCE_0310__새로운주문테스트TRADE ,
    BINANCE_0315 :BINANCE_0315__새로운주문TRADE ,
    BINANCE_0320 :BINANCE_0320__주문취소_TRADE ,
    BINANCE_0325 :BINANCE_0325__미체결주문취소TRADE ,
    BINANCE_0330 :BINANCE_0330__주문상태USER_DATA ,
    BINANCE_0335 :BINANCE_0335__현재미체결주문USER_DATA ,
    BINANCE_0340 :BINANCE_0340__모든주문USER_DATA ,
    BINANCE_0345 :BINANCE_0345__OCO신규주문TRADE ,
    BINANCE_0350 :BINANCE_0350__OCO취소TRADE ,    
    BINANCE_0355 :BINANCE_0355__OCO주문내역USER_DATA ,
    BINANCE_0360 :BINANCE_0360__OCO모든주문내역USER_DATA ,
    BINANCE_0365 :BINANCE_0365__OCO미체결내역USER_DATA ,
    BINANCE_0370 :BINANCE_0370__계정정보USER_DATA,
    BINANCE_0375 :BINANCE_0375__계정거래목록USER_DATA,
 };
   
 const PageIndex =(config:IConfig) => {
    // component does exist
    //console.log(config)
    if (typeof keysToComponentMap[config.component] !== "undefined") {
      return React.createElement(keysToComponentMap[config.component]);
    }
    // component doesn't exist yet
    return React.createElement(
      () => <div>The component {config.component} has not been created yet.</div>,
    );
  }
  export default PageIndex;