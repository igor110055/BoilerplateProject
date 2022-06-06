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
    TYPE:string;
    ASSET:string;
    AMOUNT:string;
    FROM_SYMBOL:string;
    TO_SYMBOL:string;
}

const defaultValues = {
    TYPE:"",
    ASSET:"",
    AMOUNT:"",
    FROM_SYMBOL:"",
    TO_SYMBOL:"",
};

export const BINANCE_0185__사용자범용전송= () => {   
    const menuStoreContext = useContext(MenuContext)
    const {messageAlert,messageConfirm,inlineDialog,inlineDialogClose } = menuStoreContext;
    const childRef = useRef<GridHandler>(null);
       
    useEffect(()=>{

    },[])

    const [rows,setRows] = useState([]);
    const methods = useForm<IFormInput>({ defaultValues: defaultValues});
    const { handleSubmit, reset, control,formState: { errors } ,watch,setError } = methods;

  
    const columns:OptColumn[] = [ 
        {header: 'TRAN_ID',name: 'TRAN_ID',width: 200,align : 'left',sortable : true,resizable: true,sortingType: 'desc'}
    ];

    const fnTransfer= async (data: IFormInput) => {
        
        send("BR_BINANCE_WALLET_GET_SAPI_V1_TradeFee", {
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
            <h3>User Universal Transfer (USER_DATA)</h3>
            POST /sapi/v1/asset/transfer (HMAC SHA256)
            You need to enable Permits Universal Transfer option for the api key which requests this endpoint.
            <b>Weight(IP):</b>  1
            </pre> 
            <pre className="tal lh12">
            <ul>
                <li>fromSymbol must be sent when type are ISOLATEDMARGIN_MARGIN and ISOLATEDMARGIN_ISOLATEDMARGIN</li>
                <li>toSymbol must be sent when type are MARGIN_ISOLATEDMARGIN and ISOLATEDMARGIN_ISOLATEDMARGIN</li>
            </ul>
            </pre>            
            <Stack
                mt={2}
                direction="row"
                justifyContent="flex-start"
                alignItems="flex-start"
                spacing={0.5}
                >
                <FormTextFiled  name="TYPE"  control={control}  label="TYPE(필수)"  rules={{ required: `TYPE required` }} /> 
                <FormTextFiled  name="ASSET"  control={control}  label="ASSET(필수)"   rules={{ required: `ASSET required` }} /> 
                <FormTextFiled  name="AMOUNT"  control={control}  label="AMOUNT(필수)"   rules={{ required: `AMOUNT required` }} /> 
                <FormTextFiled  name="FROM_SYMBOL"  control={control}  label="FROM_SYMBOL"  /> 
                <FormTextFiled  name="TO_SYMBOL"  control={control}  label="TO_SYMBOL"   /> 
                <Button variant="contained" color="success"  onClick={handleSubmit(fnTransfer)}>전송</Button>
            </Stack>
            <TuiGrid columns={columns} ref={childRef}   />
            </>
    )
  }
