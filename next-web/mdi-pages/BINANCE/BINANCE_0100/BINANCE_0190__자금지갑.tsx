import Grid,{GridHandler} from '@/form-components/Grid';
import { FormProvider, useForm } from "react-hook-form";
import {FormSelect} from '@/form-components/FormSelect';
import {FormTextFiled} from '@/form-components/FormTextFiled';
import React, { useState,useEffect,useRef, useContext } from "react";
import send from '@/utils/send';
import {Stack,Button,Typography,Breadcrumbs,Link} from '@mui/material'
import TuiGrid,{TuiGridHandler} from '@/form-components/TuiGrid';
import {MenuContext} from "@/store/MenuStore";
import { OptColumn } from 'tui-grid/types/options';

interface IFormInput {
    ASSET:string;
    NEED_BTC_VALUATION:string;
}

const defaultValues = {
    ASSET:"",
    NEED_BTC_VALUATION:"",
};

export const BINANCE_0190__자금지갑= () => {   
    const menuStoreContext = useContext(MenuContext)
    const {messageAlert,messageConfirm,inlineDialog,inlineDialogClose } = menuStoreContext;
    const childRef = useRef<GridHandler>(null);
       
    useEffect(()=>{

    },[])

    const [rows,setRows] = useState([]);
    const methods = useForm<IFormInput>({ defaultValues: defaultValues});
    const { handleSubmit, reset, control,formState: { errors } ,watch,setError } = methods;

  
    const columns:OptColumn[] = [ 
        {header: 'ASSET',name: 'ASSET',width: 200,align : 'left',sortable : true,resizable: true,sortingType: 'desc'}
        ,{header: 'FREE',name: 'FREE',width: 200,align : 'left',sortable : true,resizable: true,sortingType: 'desc'}
        ,{header: 'LOCKED',name: 'LOCKED',width: 200,align : 'left',sortable : true,resizable: true,sortingType: 'desc'}
        ,{header: 'FREEZE',name: 'FREEZE',width: 200,align : 'left',sortable : true,resizable: true,sortingType: 'desc'}
        ,{header: 'WITHDRAWING',name: 'WITHDRAWING',width: 200,align : 'left',sortable : true,resizable: true,sortingType: 'desc'}
        ,{header: 'BTC_VALUATION',name: 'BTC_VALUATION',width: 200,align : 'left',sortable : true,resizable: true,sortingType: 'desc'}
    ];

    const fnSearch= async (data: IFormInput) => {
        
        send("BR_BINANCE_WALLET_POST_SAPI_V1_FundingWallet", {
            brRq : 'IN_PSET'
            ,brRs : 'OUT_RSET'
            ,IN_PSET : [ data ]
        }).then(function(data:any){
            if(childRef.current){
                childRef.current.setData(data.OUT_RSET);
            }
        }).catch(function(e){
            console.log({e})
            messageAlert(e.err_msg)

        })
    }

    return (
            <>
            <pre className="tal lh12">
            <h3>Funding Wallet (USER_DATA)</h3>
            POST /sapi/v1/asset/get-funding-asset (HMAC SHA256)
            <b>Weight(IP):</b>  1
            </pre>            
            <pre className="tal lh12">
            Currently supports querying the following business assets：Binance Pay, Binance Card, Binance Gift Card, Stock Token
            </pre>            
            <Stack
                mt={2}
                direction="row"
                justifyContent="flex-start"
                alignItems="flex-start"
                spacing={0.5}
                >
                <FormTextFiled  name="ASSET"  control={control}  label="ASSET"/> 
                <FormTextFiled  name="NEED_BTC_VALUATION"  control={control}  label="NEED_BTC_VALUATION"  /> (true or false)
                <Button variant="contained" color="success"  onClick={handleSubmit(fnSearch)}>조회</Button>
            </Stack>
            <TuiGrid columns={columns} ref={childRef}   />
            </>
    )
  }
