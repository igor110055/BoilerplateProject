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
    SEARCH_NM:string;
}

const defaultValues = {
    SEARCH_NM:"",
};

export const UPBIT_0150__업비트종목주별시세= () => {   
    const menuStoreContext = useContext(MenuContext)
    const {messageAlert,messageConfirm } = menuStoreContext;
    
    const methods = useForm<IFormInput>({ defaultValues: defaultValues});
    const { handleSubmit, reset, control,formState: { errors } ,watch,setError,setValue } = methods;
    const childRef = useRef<TuiGridHandler>(null);
        
    useEffect(()=>{
        handleSubmit(fnSearch)()
    },[]);

    
    const fnSearch= async (data: IFormInput) => {
        if(childRef.current){
            childRef.current.loadData({
                                        BR:"BR_UPBIT_CANDLES_WEEKS_FIND",
                                        PARAM: {
                                            brRq : 'IN_DATA'
                                            ,brRs : 'OUT_DATA'
                                            ,IN_DATA:[data]
                                        }
                                    });

        }
        
    }

    const syncHandler= async (data: IFormInput) => {

        messageConfirm("싱크하시겠습니까?",function()  {
            //_this.showProgress();	
            send('BR_UPBIT_CANDLES_WEEKS_SYNC',{
                brRq 		: 'IN_DATA'
                ,brRs 		: ''
                ,IN_DATA:[ data ]
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
        {header: '종목명',name: 'MARKET',width: 200,align : 'center',filter : {type : 'text',showApplyBtn : true,showClearBtn : true}}
        ,{header: '캔들 기준 시각(UTC 기준)',name: 'CANDLE_DATE_TIME_UTC',width: 200,align : 'center',sortable : true,resizable: true,sortingType: 'desc'}
        ,{header: '캔들 기준 시각(KST 기준)',name: 'CANDLE_DATE_TIME_KST',width: 200,align : 'center',sortable : true,resizable: true,sortingType: 'desc'}
        ,{header: '시가',name: 'OPENING_PRICE',width: 100,align : 'right',sortable : true,resizable: true,renderer : {type: commaRenderer},sortingType: 'desc'}
        ,{header: '고가',name: 'HIGH_PRICE',width: 100,align : 'right',sortable : true,resizable: true,renderer : {type: commaRenderer},sortingType: 'desc'}
        ,{header: '저가',name: 'LOW_PRICE',width: 100,align : 'right',sortable : true,resizable: true,renderer : {type: commaRenderer},sortingType: 'desc'}
        ,{header: '종가',name: 'TRADE_PRICE',width: 200,align : 'right',sortable : true,resizable: true,renderer : {type: commaRenderer},sortingType: 'desc'}
        ,{header: '해당 캔들에서 마지막 틱이 저장된 시각',name: 'TIMESTAMP',width: 200,align : 'center',sortable : true,resizable: true,sortingType: 'desc'}
        ,{header: '누적 거래 금액',name: 'CANDLE_ACC_TRADE_PRICE',width: 200,align : 'right',sortable : true,resizable: true,renderer : {type: commaRenderer},sortingType: 'desc'}
        ,{header: '누적 거래량',name: 'CANDLE_ACC_TRADE_VOLUME',width: 200,align : 'right',sortable : true,resizable: true,renderer : {type: commaRenderer},sortingType: 'desc'}
        ,{header: '캔들 기간의 가장 첫 날',name: 'FIRST_DAY_OF_PERIOD',width: 200,align : 'center',sortable : true,resizable: true,sortingType: 'desc'}
        ,{header: '생성일',name: 'CRT_DTM',width: 200,sortable: true,align: "center"}
        ,{header: '수정일',name: 'UPDT_DTM',width: 200,sortable: true,align: "center"}
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
                    <FormTextFiled name="SEARCH_NM" control={control} label="검색" sx={{width:200}}  rules={{ required: false }}   />
                    <Button variant="contained" color="success"   onClick={handleSubmit(fnSearch)}>조회</Button>
                    <Button variant="contained" color="success" onClick={handleSubmit(syncHandler)}>주별 싱크(30주 이전)</Button>
                    업비트는 오전 9시에 종가 초기화
            </Stack>
            <TuiGrid   columns={columns}   ref={childRef} />
            </>
    )
  }
