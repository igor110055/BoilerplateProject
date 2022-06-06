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
    START_TIME:string;
    END_TIME:string;
    CURRENT:string;
    SIZE:string;
    FROM_SYMBOL:string;
    TO_SYMBOL:string;
}

const defaultValues = {
    TYPE:"",
    START_TIME:"",
    END_TIME:"",
    CURRENT:"",
    SIZE:"",
    FROM_SYMBOL:"",
    TO_SYMBOL:""
};

export const BINANCE_0186__사용자범용전송내역= () => {   
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
        ,{header: 'AMOUNT',name: 'AMOUNT',width: 200,align : 'left',sortable : true,resizable: true,sortingType: 'desc'}
        ,{header: 'TYPE',name: 'TYPE',width: 200,align : 'left',sortable : true,resizable: true,sortingType: 'desc'}
        ,{header: 'STATUS',name: 'STATUS',width: 200,align : 'left',sortable : true,resizable: true,sortingType: 'desc'}
        ,{header: 'TRAN_ID',name: 'TRAN_ID',width: 200,align : 'left',sortable : true,resizable: true,sortingType: 'desc'}
        ,{header: 'TIMESTAMP',name: 'TIMESTAMP',width: 200,align : 'left',sortable : true,resizable: true,sortingType: 'desc'}
    ];

    const fnSearch= async (data: IFormInput) => {
        
        send("BR_BINANCE_WALLET_GET_SAPI_V1_UserUniversalTransferHistory", {
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
            <h3>Query User Universal Transfer History (USER_DATA)</h3>
            GET /sapi/v1/asset/transfer (HMAC SHA256)
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
                <FormTextFiled  name="TYPE"  control={control}  label="TYPE"/> 
                <FormTextFiled  name="START_TIME"  control={control}  label="START_TIME"  /> 
                <FormTextFiled  name="END_TIME"  control={control}  label="END_TIME"  /> 
                <FormTextFiled  name="CURRENT"  control={control}  label="CURRENT"  /> 
                <FormTextFiled  name="SIZE"  control={control}  label="SIZE"  /> 
                <FormTextFiled  name="FROM_SYMBOL"  control={control}  label="FROM_SYMBOL"  /> 
                <FormTextFiled  name="TO_SYMBOL"  control={control}  label="TO_SYMBOL"   /> 
                <Button variant="contained" color="success"  onClick={handleSubmit(fnSearch)}>조회</Button>
            </Stack>
            <TuiGrid columns={columns} ref={childRef}   />
            </>
    )
  }
