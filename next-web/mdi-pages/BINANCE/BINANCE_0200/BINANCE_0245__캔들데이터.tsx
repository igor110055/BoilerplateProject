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

import * as pjtUtil from '@/utils/pjtUtil'


interface IFormInput {
    SYMBOL:string;
    INTERVAL:string;
    FROM_ID:string;
    START_TIME:string;
    END_TIME:string;
    LIMIT:string;
}

const defaultValues = {
    SYMBOL:"",
    INTERVAL:"",
    FROM_ID:"",
    START_TIME:"",
    END_TIME:"",
    LIMIT:"",
};


export const BINANCE_0245__캔들데이터= () => {   
    const menuStoreContext = useContext(MenuContext)
    const {messageAlert,messageConfirm } = menuStoreContext;
    
    const methods = useForm<IFormInput>({ defaultValues: defaultValues});
    const { handleSubmit, reset, control,formState: { errors } ,watch,setError,setValue } = methods;
    const childRef = useRef<TuiGridHandler>(null);
    const [interval,setInterval] = useState([]);

    useEffect(()=>{
        const getData = async ()=>{
            const data:any= await send("BR_CM_CD_FIND", {brRq: 'IN_DATA'
            ,brRs: 'OUT_DATA'
            ,IN_DATA: [  { GRP_CD : 'BINANCE_CANDLE_INTERVAL',  USE_YN: 'Y'}]
            });

            const tmp= data.OUT_DATA.map((m:any)=>{
                return {
                    value: m["CD"],
                    text: m["CD_NM"]            
                };
            })
            setInterval(tmp)
        }
        getData();
        handleSubmit(fnSearch)()
    },[]);
    
    const fnSearch= async (data: IFormInput) => {
        console.log({data})

        if(childRef.current){
            childRef.current.loadData({
                                        BR:"BR_BINANCE_MARKET_GET_API_V3_KlineCandlestickData",
                                        PARAM: {
                                            brRq : 'IN_DATA'
                                            ,brRs : 'OUT_RSET'
                                            ,IN_DATA: [data]
                                        }
                                    });
        }
    }

    return (
            <>
            <pre className="tal lh12">
            <h3>Kline/Candlestick Data</h3>
            GET /api/v3/klines
            Kline/candlestick bars for a symbol.
            Klines are uniquely identified by their open time.
            <b>Weight(IP):</b>  1
            </pre>   
            <Stack
                mt={2}
                direction="row"
                justifyContent="flex-start"
                alignItems="flex-start"
                spacing={0.5}
                >
                    <FormTextFiled name="SYMBOL" control={control} label="SYMBOL(필수)" sx={{width:200}}  rules={{ required: false }}   />
                    <FormSelect name="INTERVAL" control={control} label="INTERVAL(필수)(ENUM)" sx={{width:200}}   options={interval}  />
                    <FormTextFiled name="FROM_ID" control={control} label="FROM_ID" sx={{width:200}}   />
                    <FormTextFiled name="START_TIME" control={control} label="START_TIME" sx={{width:200}}   />
                    <FormTextFiled name="END_TIME" control={control} label="END_TIME" sx={{width:200}}   />
                    <FormTextFiled name="LIMIT" control={control} label="LIMIT" sx={{width:200}}   />(Default 500; max 1000.)
                    <Button variant="contained" color="success"   onClick={handleSubmit(fnSearch)}>조회</Button>
            </Stack>
            <TuiGrid   columns={
                        [
                            {header: 'OPEN_TIME',name: 'OPEN_TIME',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc',filter : {type : 'text',showApplyBtn : true,showClearBtn : true} /*text, number, select, date 4가지가 있다.*/}
                            ,{header: 'OPEN',name: 'OPEN',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc',filter : {type : 'text',showApplyBtn : true,showClearBtn : true} /*text, number, select, date 4가지가 있다.*/}
                            ,{header: 'HIGH',name: 'HIGH',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc',filter : {type : 'text',showApplyBtn : true,showClearBtn : true} /*text, number, select, date 4가지가 있다.*/}
                            ,{header: 'LOW',name: 'LOW',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc',filter : {type : 'text',showApplyBtn : true,showClearBtn : true} /*text, number, select, date 4가지가 있다.*/}
                            ,{header: 'CLOSE',name: 'CLOSE',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc',filter : {type : 'text',showApplyBtn : true,showClearBtn : true} /*text, number, select, date 4가지가 있다.*/}
                            ,{header: 'VOLUME',name: 'VOLUME',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc',filter : {type : 'text',showApplyBtn : true,showClearBtn : true} /*text, number, select, date 4가지가 있다.*/}
                            ,{header: 'CLOSE_TIME',name: 'CLOSE_TIME',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc',filter : {type : 'text',showApplyBtn : true,showClearBtn : true} /*text, number, select, date 4가지가 있다.*/}
                            ,{header: 'QUOTE_ASSET_VOLUME',name: 'QUOTE_ASSET_VOLUME',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc',filter : {type : 'text',showApplyBtn : true,showClearBtn : true} /*text, number, select, date 4가지가 있다.*/}
                            ,{header: 'NUMBER_OF_TRADES',name: 'NUMBER_OF_TRADES',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc',filter : {type : 'text',showApplyBtn : true,showClearBtn : true} /*text, number, select, date 4가지가 있다.*/}
                            ,{header: 'TAKER_BUY_BASE_ASSET_VOLUME',name: 'TAKER_BUY_BASE_ASSET_VOLUME',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc',filter : {type : 'text',showApplyBtn : true,showClearBtn : true} /*text, number, select, date 4가지가 있다.*/}
                            ,{header: 'TAKER_BUY_QUOTE_ASSET_VOLUME',name: 'TAKER_BUY_QUOTE_ASSET_VOLUME',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc',filter : {type : 'text',showApplyBtn : true,showClearBtn : true} /*text, number, select, date 4가지가 있다.*/}
                            ,{header: 'IGNORE',name: 'IGNORE',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc',filter : {type : 'text',showApplyBtn : true,showClearBtn : true} /*text, number, select, date 4가지가 있다.*/}
                            ,{header: 'SYMBOL',name: 'SYMBOL',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc',filter : {type : 'text',showApplyBtn : true,showClearBtn : true} /*text, number, select, date 4가지가 있다.*/}
                            ,{header: 'INTERVAL',name: 'INTERVAL',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc',filter : {type : 'text',showApplyBtn : true,showClearBtn : true} /*text, number, select, date 4가지가 있다.*/}
                        ]
                        }   ref={childRef} />
            </>
    )
}
