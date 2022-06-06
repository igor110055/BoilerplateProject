import TuiGrid,{TuiGridHandler} from '@/form-components/TuiGrid';
import { Button, Stack } from '@mui/material';
import { useState,useEffect, useRef, useContext } from 'react';
import { OptColumn, OptHeader } from 'tui-grid/types/options';
import {commaRenderer} from '@/form-components/tui-grid-renderer/commaRenderer'
import {commaStRenderer} from '@/form-components/tui-grid-renderer/commaStRenderer'
import {buttonRenderer} from '@/form-components/tui-grid-renderer/buttonRenderer'
import {datetimeRenderer} from '@/form-components/tui-grid-renderer/datetimeRenderer'
import {commaRendererRemoveDot} from '@/form-components/tui-grid-renderer/commaRendererRemoveDot'


import { useForm } from 'react-hook-form';
import { FormTextFiled } from '@/form-components/FormTextFiled';
import { FormSelect } from '@/form-components/FormSelect';
import { MenuContext } from '@/store/MenuStore';
import send from '@/utils/send';


interface IFormInput {
    MARKET_CD:string;
    SEARCH_NM:string;
    MARKET_WARNING:string;
}

const defaultValues = {
    MARKET_CD:"",
    SEARCH_NM:"",
    MARKET_WARNING:"",
};

export const UPBIT_0120__업비트종목시세= () => {   
    const menuStoreContext = useContext(MenuContext)
    const {messageAlert,messageConfirm } = menuStoreContext;
    
    const methods = useForm<IFormInput>({ defaultValues: defaultValues});
    const { handleSubmit, reset, control,formState: { errors } ,watch,setError,setValue } = methods;
    const childRef = useRef<TuiGridHandler>(null);

    const [marketCd,setMarketCd] = useState([]);
    const [marketWarning,setMarketWarning] = useState([]);
        
    useEffect(()=>{
        const getData = async ()=>{
            const data:any= await send("BR_CM_CD_FIND", {brRq: 'IN_DATA'
            ,brRs: 'OUT_DATA'
            ,IN_DATA: [  { GRP_CD : 'UPBIT_MARKET_CD',  USE_YN: 'Y'}]
            });

            const tmp= data.OUT_DATA.map((m:any)=>{
                return {
                    value: m["CD"],
                    text: m["CD_NM"]            
                };
            })
            setMarketCd(tmp)

            const data2:any= await send("BR_CM_CD_FIND", {brRq: 'IN_DATA'
            ,brRs: 'OUT_DATA'
            ,IN_DATA: [  { GRP_CD : 'UPBIT_MARKET_WARNING',  USE_YN: 'Y'}]
            });

            const tmp2= data2.OUT_DATA.map((m:any)=>{
                return {
                    value: m["CD"],
                    text: m["CD_NM"]            
                };
            })
            setMarketWarning(tmp2)
        }
        getData();
        handleSubmit(fnSearch)()
    },[]);

    
    const fnSearch= async (data: IFormInput) => {
        if(childRef.current){
            childRef.current.loadData({
                                        BR:"BR_UPBIT_retrieveTicker",
                                        PARAM: {
                                            brRq : 'IN_DATA'
                                            ,brRs : 'OUT_DATA'
                                            ,IN_DATA:[data]
                                        }
                                    });

        }
        
    }

    const syncHandler=(event: React.MouseEvent<HTMLButtonElement>) => {

        messageConfirm("싱크하시겠습니까?",function()  {
            //_this.showProgress();	
            send('BR_UPBIT_TICKER_SYNC',{
                brRq 		: 'IN_DATA'
                ,brRs 		: ''
                ,IN_DATA:[ {} ]
            }).then(function(data){
                //_this.hideProgress();
                if(data){
                    messageAlert("싱크되었습니다",function()  {
                        handleSubmit(fnSearch)()
                    });
                }
            })                      
        })
    }


      const columns:OptColumn[] = [ 
        {header : 'UPBIT_매도/매수',name : 'BTN_SELL',renderer : {type: buttonRenderer,options : {txt : '매도/매수',fn : function(el:any,data:any){}}},width : 140,sortable : true,align : "center"}
        ,{header: '종목명',name: 'MARKET',width: 80,align : 'center',filter : {type : 'text',showApplyBtn : true,showClearBtn : true}}
        ,{header: '최근 거래 일자(UTC)',name: 'TRADE_DATE',width: 100,align : 'center',sortable : true,resizable: true,sortingType: 'desc'}
        ,{header: '최근 거래 시각(UTC)',name: 'TRADE_TIME',width: 100,align : 'center',sortable : true,resizable: true,sortingType: 'desc'}
        ,{header: '최근 거래 일자(KST)',name: 'TRADE_DATE_KST',width: 100,align : 'center',sortable : true,resizable: true,sortingType: 'desc'}
        ,{header: '최근 거래 시각(KST)',name: 'TRADE_TIME_KST',width: 100,align : 'center',sortable : true,resizable: true,sortingType: 'desc'}
        ,{header: '시가2',name: 'OPENING_PRICE',width: 100,align : 'right',sortable : true,resizable: true,sortingType: 'desc'}
        ,{header: '고가',name: 'HIGH_PRICE',width: 100,align : 'right',sortable : true,resizable: true,sortingType: 'desc'}
        ,{header: '저가',name: 'LOW_PRICE',width: 100,align : 'right',sortable : true,resizable: true,sortingType: 'desc'}
        ,{header: '종가',name: 'TRADE_PRICE',width: 200,align : 'right',sortable : true,resizable: true,sortingType: 'desc'}
        ,{header: '전일 종가',name: 'PREV_CLOSING_PRICE',width: 200,align : 'right',sortable : true,resizable: true,sortingType: 'desc'}
        ,{header: '변화',name: 'CHANGE',width: 200,align : 'right',sortable : true,resizable: true,sortingType: 'desc'}
        ,{header: '변화액의 절대값',name: 'CHANGE_PRICE',width: 200,align : 'right',sortable : true,resizable: true,renderer : {type: commaRendererRemoveDot},sortingType: 'desc'}
        ,{header: '변화율의 절대값',name: 'CHANGE_RATE',width: 200,align : 'right',sortable : true,resizable: true,renderer : {type: commaRendererRemoveDot},sortingType: 'desc'}
        ,{header: '부호가 있는 변화액',name: 'SIGNED_CHANGE_PRICE',width: 200,align : 'right',sortable : true,resizable: true,renderer : {type: commaRendererRemoveDot},sortingType: 'desc'}
        ,{header: '부호가 있는 변화율',name: 'SIGNED_CHANGE_RATE',width: 200,align : 'right',sortable : true,resizable: true,renderer : {type: commaRendererRemoveDot},sortingType: 'desc'}
        ,{header: '가장 최근 거래량',name: 'TRADE_VOLUME',width: 200,align : 'right',sortable : true,resizable: true,renderer : {type: commaRendererRemoveDot},sortingType: 'desc'}
        ,{header: '누적 거래대금(UTC 0시 기준)',name: 'ACC_TRADE_PRICE',width: 200,align : 'right',sortable : true,resizable: true,renderer : {type: commaRendererRemoveDot},sortingType: 'desc'}
        ,{header: '24시간 누적 거래대금',name: 'ACC_TRADE_PRICE_24H',width: 200,align : 'right',sortable : true,resizable: true,renderer : {type: commaRendererRemoveDot},sortingType: 'desc'}
        ,{header: '누적 거래량(UTC 0시 기준)',name: 'ACC_TRADE_VOLUME',width: 200,align : 'right',sortable : true,resizable: true,renderer : {type: commaRendererRemoveDot},sortingType: 'desc'}
        ,{header: '24시간 누적 거래량',name: 'ACC_TRADE_VOLUME_24H',width: 200,align : 'right',sortable : true,resizable: true,renderer : {type: commaRendererRemoveDot},sortingType: 'desc'}
        ,{header: '24시간 누적 거래량',name: 'HIGHEST_52_WEEK_PRICE',width: 200,align : 'right',sortable : true,resizable: true,renderer : {type: commaRendererRemoveDot},sortingType: 'desc'}
        ,{header: '52주 신고가 달성일',name: 'HIGHEST_52_WEEK_DATE',width: 200,align : 'right',sortable : true,resizable: true,sortingType: 'desc'}
        ,{header: '52주 신저가',name: 'LOWEST_52_WEEK_PRICE',width: 200,align : 'right',sortable : true,resizable: true,renderer : {type: commaRendererRemoveDot},sortingType: 'desc'}
        ,{header: '52주 신저가 달성일',name: 'LOWEST_52_WEEK_DATE',width: 200,align : 'right',sortable : true,resizable: true,sortingType: 'desc'}
        ,{header: '해당 캔들에서 마지막 틱이 저장된 시각',name: 'TIMESTAMP',width: 200,align : 'center',sortable : true,resizable: true,sortingType: 'desc'}
        ,{header: '생성일',name: 'CRT_DTM',width: 200,sortable: true,align: "center"}
    ];

    return (
            <>
            <Stack
                mt={2}
                direction="row"
                justifyContent="flex-start"
                alignItems="flex-start"
                spacing={0.5}
                >
                    <FormSelect name="CURRENCY" control={control} label="업비트 통화코드" sx={{width:200}} rules={{ required: false }} options={marketCd}  />
                    <FormTextFiled name="SEARCH_NM" control={control} label="검색" sx={{width:200}}  rules={{ required: false }}   />
                    <FormSelect name="MARKET_WARNING" control={control} label="유의 종목 여부" sx={{width:200}} rules={{ required: false }} options={marketWarning}  />
                    <Button variant="contained" color="success"   onClick={handleSubmit(fnSearch)}>조회</Button>
                    <Button variant="contained" color="success" onClick={syncHandler}>싱크</Button>
                    업비트는 오전 9시에 종가 초기화
            </Stack>
            <TuiGrid   columns={columns}   ref={childRef} />
            </>
    )
  }
