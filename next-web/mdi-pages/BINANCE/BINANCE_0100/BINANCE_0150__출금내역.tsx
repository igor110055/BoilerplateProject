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
    COIN:string;
    STATUS:string;
    START_TIME:string;
    END_TIME:string;
    OFFSET:string;
    LIMIT:string;
}

const defaultValues = {
    COIN:"",
    STATUS:"",
    START_TIME:"",
    END_TIME:"",
    OFFSET:"",
    LIMIT:""
};


export const BINANCE_0150__출금내역= () => {   
    const menuStoreContext = useContext(MenuContext)
    const {messageAlert,messageConfirm,inlineDialog,inlineDialogClose } = menuStoreContext;
    const childRef = useRef<GridHandler>(null);
    useEffect(()=>{

    },[])

    const [rows,setRows] = useState([]);
    const methods = useForm<IFormInput>({ defaultValues: defaultValues});
    const { handleSubmit, reset, control,formState: { errors } ,watch,setError } = methods;

  
    const columns:OptColumn[] = [ 
        {header: 'ADDRESS',name: 'ADDRESS',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc'}
        ,{header: 'AMOUNT',name: 'AMOUNT',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc'}
        ,{header: 'APPLY_TIME',name: 'APPLY_TIME',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc'}
        ,{header: 'COIN',name: 'COIN',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc'}
        ,{header: 'ID',name: 'ID',width: 150,align : 'left',sortable : true,resizable: true,sortingType: 'desc'}
        ,{header: 'WITHDRAW_ORDER_ID',name: 'WITHDRAW_ORDER_ID',width: 150,align : 'left',sortable : true,resizable: true,sortingType: 'desc'}
        ,{header: 'NETWORK',name: 'NETWORK',width: 150,align : 'left',sortable : true,resizable: true,sortingType: 'desc'}
        ,{header: 'TRANSFER_TYPE',name: 'TRANSFER_TYPE',width: 150,align : 'left',sortable : true,resizable: true,sortingType: 'desc'}
        ,{header: 'STATUS',name: 'STATUS',width: 150,align : 'left',sortable : true,resizable: true,sortingType: 'desc'}
        ,{header: 'TRANSACTION_FEE',name: 'TRANSACTION_FEE',width: 150,align : 'left',sortable : true,resizable: true,sortingType: 'desc'}
        ,{header: 'CONFIRM_NO',name: 'CONFIRM_NO',width: 150,align : 'left',sortable : true,resizable: true,sortingType: 'desc'}
        ,{header: 'TX_ID',name: 'TX_ID',width: 150,align : 'left',sortable : true,resizable: true,sortingType: 'desc'}
    ];


    const fnSearch= async (data: IFormInput) => {
        if(childRef.current){
            childRef.current.loadData({
                                        BR:"BR_BINANCE_WALLET_GET_SAPI_V1_DepositHistory",
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
            <pre className="tal lh12">
            <h3>Withdraw History (supporting network) (USER_DATA)</h3>
            GET /sapi/v1/capital/withdraw/history (HMAC SHA256)
            Fetch withdraw history.
            <b>Weight(IP):</b>  1
            </pre> 
            <Stack
                mt={2}
                direction="row"
                justifyContent="flex-start"
                alignItems="flex-start"
                spacing={0.5}
                >
                    <FormTextFiled  name="COIN"  control={control}  label="COIN" /> 
                    <FormTextFiled  name="WITHDRAW_ORDER_ID"  control={control}  label="WITHDRAW_ORDER_ID"  /> 
                    <FormTextFiled  name="STATUS"  control={control}  label="STATUS" /> 
                    <FormTextFiled  name="OFFSET"  control={control}  label="OFFSET" /> 
                    <FormTextFiled  name="LIMIT"  control={control}  label="LIMIT" /> 
                    <FormTextFiled  name="START_TIME"  control={control}  label="START_TIME" /> 
                    <Button variant="contained" color="success"  onClick={handleSubmit(fnSearch)}>조회</Button>
            </Stack>
            <TuiGrid columns={columns} ref={childRef}   />
            </>
    )
  }
