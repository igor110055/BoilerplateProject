import Grid,{GridHandler} from '@/form-components/Grid';
import { FormProvider, useForm } from "react-hook-form";
import {FormSelect} from '@/form-components/FormSelect';
import {FormTextFiled} from '@/form-components/FormTextFiled';
import React, { useState,useEffect,useRef, useContext } from "react";
import send from '@/utils/send';
import {Stack,Button,Typography,Breadcrumbs,Link} from '@mui/material'
import DataGrid, { Column,SelectColumn } from 'react-data-grid';
import { CalculatedColumn } from 'react-data-grid';
import {MenuContext} from "@/store/MenuStore";
import { dark } from 'react-syntax-highlighter/dist/cjs/styles/prism';
import { Prism as SyntaxHighlighter } from 'react-syntax-highlighter';

interface IFormInput {
}

const defaultValues = {
};


export const FX_0110__KRW업비트TO바이낸스= () => {   
    const menuStoreContext = useContext(MenuContext)
    const {messageAlert,messageConfirm,inlineDialog,inlineDialogClose } = menuStoreContext;
    const childRef = useRef<GridHandler>(null);
    useEffect(()=>{
        console.log("rrrr")
    })

    const [rows,setRows] = useState([]);
    const methods = useForm<IFormInput>({ defaultValues: defaultValues});
    const { handleSubmit, reset, control,formState: { errors } ,watch,setError } = methods;

  
    const columns = [
        { key: 'FX_SEQ', name: 'FX_SEQ', width: 140 ,  sortable: true},
        { key: 'BALANCE_TYPE', name: 'BALANCE_TYPE', width: 140,  sortable: true , dataType: 'number'},
        { key: "BTN_UPBIT_SELL", name: 'UPBIT매도/매수', width: 140 ,  sortable: true , resizable:true},
        { key: 'FST_UPBIT_MARKET', name: '코인', width: 100 ,  sortable: true, resizable:true},
        { key: 'FST_BINANCE_SYMBOL', name: '바이낸스', width: 100 ,  sortable: true, resizable:true},
        { key: 'FST_UPBIT_PRICE', name: '업비트', width: 80 , resizable:true},
        { key: 'FST_BINANCE_PRICE_KRW', name: '바이낸스', width: 100 , resizable:true},
        { key: 'FST_BINANCE_PRICE_USD', name: '$바이낸스', width: 100 , resizable:true},
        { key: 'FST_GAP', name: 'GAP', width: 80 , resizable:true},
        { key: 'FST_PER', name: 'PER', width: 80 },
        { key: 'FST_UPBIT_CNT', name: '수량', width: 100 },
        { key: 'FST_UPBIT_BID_FEE_AMT', name: '수수료', width: 100 },
        { key: 'FST_UPBIT_WITHDRAW_FEE_AMT', name: '출금', width: 100 },
        { key: 'FST_UPBIT_SUM_AMT', name: 'PER', width: 100 },
        { key: 'FST_PER', name: '총매수', width: 100 },
        { key: 'FST_BINANCE_SUM_AMT', name: '총매도', width: 100 },
        { key: 'FST_PER', name: 'PER', width: 100 },
        { key: 'FST_SUM_GAP', name: 'GAP', width: 100 },

        { key: 'FST_SUM_PER', name: 'PER', width: 100 },
        { key: 'LST_UPBIT_MARKET', name: '업비트', width: 120 },
        { key: 'LST_BINANCE_SYMBOL', name: '바이낸스', width: 120 },
        { key: 'LST_BINANCE_PRICE_KRW', name: '바이낸스', width: 120 },
        { key: 'LST_BINANCE_PRICE_USD', name: '$바이낸스', width: 120 },
        { key: 'LST_UPBIT_PRICE', name: '업비트', width: 120 },
        { key: 'LST_GAP', name: 'GAP', width: 80 },

        { key: 'LST_PER', name: 'PER', width: 80 },
        { key: 'LST_BINANCE_CNT', name: '수량', width: 80 },
        { key: 'LST_BINANCE_MAKER_COMMISSION_AMT', name: '수수료', width: 80 },
        { key: 'LST_BINANCE_WITHDRAW_FEE_AMT', name: '출금', width: 80 },
        { key: 'LST_BINANCE_SUM_AMT', name: '총매수', width: 80 },
        { key: 'LST_UPBIT_SUM_AMT', name: '총매도', width: 80 },
        { key: 'LST_SUM_GAP', name: 'GAP', width: 80 },
        { key: 'LST_SUM_PER', name: 'PER', width: 80 },
        { key: 'FINAL_GAP', name: 'GAP', width: 80 },
        { key: 'FINAL_PER', name: 'PER', width: 80 },
        { key: 'BALANCE_TYPE', name: 'TYPE', width: 80 },
        { key: 'UPBIT_KRW_BALANCE', name: 'U_KRW', width: 80 },
        { key: 'BINANCE_USDT_BALANCE', name: 'B_USDT', width: 80 },
        { key: 'BINANCE_KRW_BALANCE', name: 'B_KRW', width: 80 },
        { key: 'MIN_KRW_BALANCE', name: 'MIN_KRW', width: 100 },

        { key: 'BASE_KRW_BALANCE', name: 'BASE_KRW', width: 100 },
        { key: 'CRT_DTM', name: '생성일', width: 160 },
        { key: 'ERR', name: 'ERR', width: 160 }
    ];


    const fnSearch= async (data: IFormInput) => {
        if(childRef.current){
            childRef.current.loadData({
                                        BR:"BR_FX_UPBIT_retrieveTmpUpbitTOBinance",
                                        PARAM: {
                                            brRq : 'IN_PSET'
                                            ,brRs : 'OUT_RSET'
                                            ,IN_PSET:[data]
                                        }
                                    });

        }
        
    }

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
            <Grid columns={columns} checkbox={false} ref={childRef}   />
            </>
    )
  }
