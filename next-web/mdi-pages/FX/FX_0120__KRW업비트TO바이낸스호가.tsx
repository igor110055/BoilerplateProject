import TuiGrid,{TuiGridHandler} from '@/form-components/TuiGrid';
import { Button, Stack } from '@mui/material';
import { useState,useEffect, useRef } from 'react';
import { OptColumn, OptHeader } from 'tui-grid/types/options';
import {commaRenderer} from '@/form-components/tui-grid-renderer/commaRenderer'
import {commaStRenderer} from '@/form-components/tui-grid-renderer/commaStRenderer'
import {buttonRenderer} from '@/form-components/tui-grid-renderer/buttonRenderer'
import {datetimeRenderer} from '@/form-components/tui-grid-renderer/datetimeRenderer'
import { useForm } from 'react-hook-form';


interface IFormInput {
}

const defaultValues = {
};




export const FX_0120__KRW업비트TO바이낸스호가= () => {   
    const methods = useForm<IFormInput>({ defaultValues: defaultValues});
    const { handleSubmit, reset, control,formState: { errors } ,watch,setError } = methods;

    const childRef = useRef<TuiGridHandler>(null);

    
    const fnSearch= async (data: IFormInput) => {
        if(childRef.current){
            childRef.current.loadData({
                                        BR:"BR_FX_UPBIT_retrieveTmpUpbitTOBinanceHoga",
                                        PARAM: {
                                            brRq : 'IN_PSET'
                                            ,brRs : 'OUT_RSET'
                                            ,IN_PSET:[data]
                                        }
                                    });

        }
        
    }

    const header:OptHeader =   {
        height: 60,
        complexColumns: [
         {header: '매수시점-단가',name: 'mergeColumn1',childNames: ['BTN_UPBIT_SELL','FST_COIN','FST_UPBIT_MARKET','FST_BINANCE_SYMBOL','FST_UPBIT_PRICE','FST_BINANCE_PRICE_KRW','FST_BINANCE_PRICE_USD','FST_GAP','FST_PER']} 
        ,{header: '매수시점-총',name: 'mergeColumn2',childNames: ['FST_UPBIT_CNT' ,'FST_UPBIT_BID_FEE_AMT','FST_UPBIT_WITHDRAW_FEE_AMT','FST_UPBIT_SUM_AMT','FST_BINANCE_SUM_AMT' ,'FST_SUM_GAP','FST_SUM_PER']} 
        ,{header: '매도시점-단가',name: 'mergeColumn3',childNames: ['LST_COIN','LST_UPBIT_MARKET','BTN_BINANCE_BUY','LST_BINANCE_SYMBOL','LST_BINANCE_PRICE_KRW','LST_BINANCE_PRICE_USD','LST_UPBIT_PRICE','LST_GAP','LST_PER']}  
        ,{header: '매도시점-총',name: 'mergeColumn4',childNames: ['LST_BINANCE_CNT','LST_BINANCE_MAKER_COMMISSION_AMT','LST_BINANCE_WITHDRAW_FEE_AMT','LST_BINANCE_SUM_AMT','LST_UPBIT_SUM_AMT','LST_SUM_GAP','LST_SUM_PER']}   
        ,{header: '총차액',name: 'mergeColumn5',childNames: ['FINAL_GAP','FINAL_PER']}   
        ,{header: '잔액',name: 'mergeColumn0',childNames: ['BALANCE_TYPE','UPBIT_KRW_BALANCE','BINANCE_USDT_BALANCE','BINANCE_KRW_BALANCE','MIN_KRW_BALANCE','BASE_KRW_BALANCE']
        }                    
        ]
    }
    
      const columns:OptColumn[] = [
        { header: 'FX_SEQ', name: 'FX_SEQ', width: 100, align : 'center', sortable : false, resizable: true, sortingType: 'desc' }
        ,{header : 'UPBIT매도/매수', name : 'BTN_UPBIT_SELL',width : 90,sortable : false,align : "center", renderer : {type: buttonRenderer,options : {txt : '매도/매수',fn : function(el:any,data:any){}}},}
        ,{header: 'FST_COIN',name: 'FST_COIN',width: 80,align : 'center',sortable : false,resizable: true,sortingType: 'desc'}
        ,{header: '코인',name: 'FST_UPBIT_MARKET',width: 80,align : 'center',sortable : false,resizable: true,sortingType: 'desc'}
        ,{header: '바이낸스',name: 'FST_BINANCE_SYMBOL',width: 80,align : 'center',sortable : false,resizable: true,sortingType: 'desc'}
        ,{header: '업비트',name: 'FST_UPBIT_PRICE',width: 40,align : 'right',sortable : false,resizable: true,sortingType: 'desc',renderer : {type: commaRenderer},}
        ,{header: '바이낸스',name: 'FST_BINANCE_PRICE_KRW',width: 50,align : 'right',sortable : false,resizable: true,sortingType: 'desc',renderer : {type: commaRenderer},}
        ,{header: '$바이낸스',name: 'FST_BINANCE_PRICE_USD',width: 60,align : 'right',sortable : false,resizable: true,sortingType: 'desc',renderer : {type: commaRenderer},}
        ,{header: 'GAP',name: 'FST_GAP',width: 40,align : 'right',sortable : false,resizable: true,renderer : {type: commaStRenderer,options : {src : 'FST_BINANCE_PRICE',tgt : 'FST_UPBIT_PRICE'}},sortingType: 'desc'}
        ,{header: 'PER',name: 'FST_PER',width: 40,align : 'right',sortable : false,resizable: true,sortingType: 'desc',renderer : {type: commaStRenderer,options : {src : 'FST_BINANCE_PRICE',tgt : 'FST_UPBIT_PRICE'}},}
        ,{header: '수량',name: 'FST_UPBIT_CNT',width: 40,align : 'right',sortable : false,resizable: true,sortingType: 'desc',renderer : {type: commaRenderer},}
        ,{header: '수수료',name: 'FST_UPBIT_BID_FEE_AMT',width: 40,align : 'right',sortable : false,resizable: true,sortingType: 'desc',renderer : {type: commaRenderer},}
        ,{header: '출금',name: 'FST_UPBIT_WITHDRAW_FEE_AMT',width: 40,align : 'right',sortable : false,resizable: true,sortingType: 'desc',renderer : {type: commaRenderer},}
        ,{header: '총매수',name: 'FST_UPBIT_SUM_AMT',width: 80,align : 'right',sortable : false,resizable: true,sortingType: 'desc',renderer : {type: commaRenderer},}
        ,{header: '총매도',name: 'FST_BINANCE_SUM_AMT',width: 80,align : 'right',sortable : false,resizable: true,sortingType: 'desc',renderer : {type: commaRenderer},}
        ,{header: 'GAP',name: 'FST_SUM_GAP',width: 60,align : 'right',sortable : false,resizable: true,sortingType: 'desc',renderer : {type: commaStRenderer,options : {src : 'FST_BINANCE_SUM_AMT',tgt : 'FST_UPBIT_SUM_AMT'}},}
        ,{header: 'PER',name: 'FST_SUM_PER',width: 60,align : 'right',sortable : false,resizable: true,sortingType: 'desc',renderer : {type: commaStRenderer,options : {src : 'FST_BINANCE_SUM_AMT',tgt : 'FST_UPBIT_SUM_AMT'}},}
        ,{header: 'LST_COIN',name: 'LST_COIN',width: 70,align : 'center',sortable : false,resizable: true,sortingType: 'desc'}
        ,{header: '업비트',name: 'LST_UPBIT_MARKET',width: 80,align : 'center',sortable : false,resizable: true,sortingType: 'desc'}
        ,{header : '(B)매수',name : 'BTN_BINANCE_BUY',renderer : {type: buttonRenderer,options : {txt : '(B)매수',fn : function(el:any,data:any){}}},width : 90,sortable : false,align : "center"}
        ,{header: '바이낸스',name: 'LST_BINANCE_SYMBOL',width: 80,align : 'center',sortable : false,resizable: true,sortingType: 'desc'}
        ,{header: '바이낸스',name: 'LST_BINANCE_PRICE_KRW',width: 50,align : 'right',sortable : false,resizable: true,sortingType: 'desc',renderer : {type: commaRenderer},}
        ,{header: '$바이낸스',name: 'LST_BINANCE_PRICE_USD',width: 60,align : 'right',sortable : false,resizable: true,sortingType: 'desc',renderer : {type: commaRenderer},}
        ,{header: '업비트',name: 'LST_UPBIT_PRICE',width: 40,align : 'right',sortable : false,resizable: true,sortingType: 'desc',renderer : {type: commaRenderer},}
        ,{header: 'GAP',name: 'LST_GAP',width: 40,align : 'right',sortable : false,resizable: true,renderer : {type: commaStRenderer,options : {src : 'LST_BINANCE_PRICE_KRW',tgt : 'LST_UPBIT_PRICE'}},sortingType: 'desc'}
        ,{header: 'PER',name: 'LST_PER',width: 40,align : 'right',sortable : false,resizable: true,sortingType: 'desc',renderer : {type: commaStRenderer,options : {src : 'LST_BINANCE_PRICE_KRW',tgt : 'LST_UPBIT_PRICE'}},}
        ,{header: '수량',name: 'LST_BINANCE_CNT',width: 40,align : 'right',sortable : false,resizable: true,sortingType: 'desc',renderer : {type: commaRenderer},}
        ,{header: '수수료',name: 'LST_BINANCE_MAKER_COMMISSION_AMT',width: 40,align : 'right',sortable : false,resizable: true,sortingType: 'desc',renderer : {type: commaRenderer},}
        ,{header: '출금',name: 'LST_BINANCE_WITHDRAW_FEE_AMT',width: 40,align : 'right',sortable : false,resizable: true,sortingType: 'desc',renderer : {type: commaRenderer},}
        ,{header: '총매수',name: 'LST_BINANCE_SUM_AMT',width: 60,align : 'right',sortable : false,resizable: true,sortingType: 'desc',renderer : {type: commaRenderer},}
        ,{header: '총매도',name: 'LST_UPBIT_SUM_AMT',width: 60,align : 'right',sortable : false,resizable: true,sortingType: 'desc',renderer : {type: commaRenderer},}
        ,{header: 'GAP',name: 'LST_SUM_GAP',width: 40,align : 'right',sortable : false,resizable: true,sortingType: 'desc',renderer : {type: commaStRenderer,options : {src : 'LST_BINANCE_SUM_AMT',tgt : 'LST_UPBIT_SUM_AMT'}},}
        ,{header: 'PER',name: 'LST_SUM_PER',width: 40,align : 'right',sortable : false,resizable: true,sortingType: 'desc',renderer : {type: commaStRenderer,options : {src : 'LST_BINANCE_SUM_AMT',tgt : 'LST_UPBIT_SUM_AMT'}},}
        ,{header: 'GAP',name: 'FINAL_GAP',width: 40,align : 'right',sortable : false,resizable: true,sortingType: 'desc',renderer : {type: commaStRenderer,options : {src : 'FST_UPBIT_SUM_AMT',tgt : 'LST_UPBIT_SUM_AMT'}},}
        ,{header: 'PER',name: 'FINAL_PER',width: 40,align : 'right',sortable : false,resizable: true,sortingType: 'desc',renderer : {type: commaStRenderer,options : {src : 'FST_UPBIT_SUM_AMT',tgt : 'LST_UPBIT_SUM_AMT'}},}
        ,{header: 'TYPE',name: 'BALANCE_TYPE',width: 60,align : 'center',sortable : false,resizable: true,sortingType: 'desc'}
        ,{header: 'U_KRW',name: 'UPBIT_KRW_BALANCE',width: 70,align : 'right',sortable : false,resizable: true,sortingType: 'desc',renderer : {type: commaRenderer}}
        ,{header: 'B_USDT',name: 'BINANCE_USDT_BALANCE',width: 60,align : 'right',sortable : false,resizable: true,sortingType: 'desc',renderer : {type: commaRenderer}}
        ,{header: 'B_KRW',name: 'BINANCE_KRW_BALANCE',width: 60,align : 'right',sortable : false,resizable: true,sortingType: 'desc',renderer : {type: commaRenderer}}
        ,{header: 'MIN_KRW',name: 'MIN_KRW_BALANCE',width: 70,align : 'right',sortable : false,resizable: true,sortingType: 'desc',renderer : {type: commaRenderer}}
        ,{header: 'BASE_KRW',name: 'BASE_KRW_BALANCE',width: 70,align : 'right',sortable : false,resizable: true,sortingType: 'desc',renderer : {type: commaRenderer}}
        ,{header : '생성일',name : 'CRT_DTM',renderer : {type : datetimeRenderer,options : {format : 'yyyy-MM-DD HH:mm' /*YYYYMMDDHHmmss    이게 풀양식이다.*/,source : 'YYYYMMDDHHmmss' /*TIME 초, YYYYMMDD , YYYYMMDDHHmm,  YYYYMMDDHHmmss  */}},width : 100,sortable : false,align : "center"}
        ,{header : 'ERR',name : 'ERR',width : 40,sortable : false,align : "right"}];

    return (
            <>
            <Stack
                mt={2}
                direction="row"
                justifyContent="flex-start"
                alignItems="flex-start"
                spacing={0.5}
                >
                    <Button variant="contained" color="success"  onClick={handleSubmit(fnSearch)}>조회</Button>
            </Stack>
            <TuiGrid   columns={columns}   ref={childRef} header={header} />
            </>
    )
  }
