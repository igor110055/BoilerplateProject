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
}

const defaultValues = {

};

export const BINANCE_0195__API키권한= () => {   
    const menuStoreContext = useContext(MenuContext)
    const {messageAlert,messageConfirm,inlineDialog,inlineDialogClose } = menuStoreContext;
    const childRef = useRef<GridHandler>(null);
       
    useEffect(()=>{

    },[])

    const [rows,setRows] = useState([]);
    const methods = useForm<IFormInput>({ defaultValues: defaultValues});
    const { handleSubmit, reset, control,formState: { errors } ,watch,setError } = methods;

  
    const columns:OptColumn[] = [ 
        {header: 'IS_RESTRICT',name: 'IS_RESTRICT',width: 200,align : 'left',sortable : true,resizable: true,sortingType: 'desc'}
        ,{header: 'CREATE_TIME',name: 'CREATE_TIME',width: 200,align : 'left',sortable : true,resizable: true,sortingType: 'desc'}
        ,{header: 'ENABLE_WITHDRAWALS',name: 'ENABLE_WITHDRAWALS',width: 200,align : 'left',sortable : true,resizable: true,sortingType: 'desc'}
        ,{header: 'ENABLE_INTERNAL_TRANSFER',name: 'ENABLE_INTERNAL_TRANSFER',width: 200,align : 'left',sortable : true,resizable: true,sortingType: 'desc'}
        ,{header: 'PERMITS_UNIVERSAL_TRANSFER',name: 'PERMITS_UNIVERSAL_TRANSFER',width: 200,align : 'left',sortable : true,resizable: true,sortingType: 'desc'	}
        ,{header: 'ENABLE_VANILLA_OPTIONS',name: 'ENABLE_VANILLA_OPTIONS',width: 200,align : 'left',sortable : true,resizable: true,sortingType: 'desc'}
        ,{header: 'ENABLE_READING',name: 'ENABLE_READING',width: 200,align : 'left',sortable : true,resizable: true,sortingType: 'desc'}
        ,{header: 'ENABLE_FUTURES',name: 'ENABLE_FUTURES',width: 200,align : 'left',sortable : true,resizable: true,sortingType: 'desc'}
        ,{header: 'ENABLE_MARGIN',name: 'ENABLE_MARGIN',width: 200,align : 'left',sortable : true,resizable: true,sortingType: 'desc'}
        ,{header: 'ENABLE_SPOT_AND_MARGIN_TRADING',name: 'ENABLE_SPOT_AND_MARGIN_TRADING',width: 200,align : 'left',sortable : true,resizable: true,sortingType: 'desc'}
        ,{header: 'TRADING_AUTHORITY_EXPIRATION_TIME',name: 'TRADING_AUTHORITY_EXPIRATION_TIME',width: 200,align : 'left',sortable : true,resizable: true,sortingType: 'desc'}
    ];

    const fnSearch= async (data: IFormInput) => {
        
        send("BR_BINANCE_WALLET_GET_SAPI_V1_GetApiKeyPermission", {
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
            <h3>Get API Key Permission (USER_DATA)</h3>
            GET /sapi/v1/account/apiRestrictions (HMAC SHA256)
            <b>Weight(IP):</b>  1
            </pre>            
            <Stack
                mt={2}
                direction="row"
                justifyContent="flex-start"
                alignItems="flex-start"
                spacing={0.5}
                >
                <Button variant="contained" color="success"  onClick={handleSubmit(fnSearch)}>조회</Button>
            </Stack>
            <TuiGrid columns={columns} ref={childRef}   />
            </>
    )
  }
