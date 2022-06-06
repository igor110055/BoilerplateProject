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
}

const defaultValues = {
    ASSET:""
};


export const BINANCE_0175__DUST전송= () => {   
    const menuStoreContext = useContext(MenuContext)
    const {messageAlert,messageConfirm,inlineDialog,inlineDialogClose } = menuStoreContext;
    const childRef = useRef<GridHandler>(null);
    const childTransferRef = useRef<GridHandler>(null);
    
    useEffect(()=>{

    },[])

    const [rows,setRows] = useState([]);
    const methods = useForm<IFormInput>({ defaultValues: defaultValues});
    const { handleSubmit, reset, control,formState: { errors } ,watch,setError } = methods;

  
    const columns:OptColumn[] = [ 
        {header: 'TOTAL_SERVICE_CHARGE',name: 'TOTAL_SERVICE_CHARGE',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc'}
        ,{header: 'TOTAL_TRANSFERED',name: 'TOTAL_TRANSFERED',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc'}
    ];



    const columns_transfer:OptColumn[] = [ 
        {header: 'AMOUNT',name: 'AMOUNT',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc'}
        ,{header: 'FROM_ASSET',name: 'FROM_ASSET',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc'}
        ,{header: 'OPERATE_TIME',name: 'OPERATE_TIME',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc'}
        ,{header: 'SERVICE_CHARGE_AMOUNT',name: 'SERVICE_CHARGE_AMOUNT',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc'}
        ,{header: 'TRAN_ID',name: 'TRAN_ID',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc'}
        ,{header: 'TRANSFERED_AMOUNT',name: 'TRANSFERED_AMOUNT',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc'}
    ];



    

    const fnSearch= async (data: IFormInput) => {
        
        send("BR_BINANCE_WALLET_POST_SAPI_V1_DustTransfer", {
            brRq : 'IN_PSET'
            ,brRs : 'OUT_RSET,OUT_RSET_DATA__TRANSFER_RESULT'
            ,IN_PSET : [ data ]
        }).then(function(data:any){
            if(childRef.current){
                childRef.current.setData(data.OUT_RSET);
                if(childTransferRef.current){
                    childTransferRef.current.setData(data.OUT_RSET_DATA__TRANSFER_RESULT);
                }
            }
        }).catch(function(e){
            console.log({e})
            messageAlert(e.err_msg)

        })
    }

    return (
            <>
            <pre className="tal lh12">
            <h3>Dust Transfer (USER_DATA)</h3>
            POST /sapi/v1/asset/dust (HMAC SHA256)
            Convert dust assets to BNB.
            <b>Weight(IP):</b>  10
            </pre> 
            <Stack
                mt={2}
                direction="row"
                justifyContent="flex-start"
                alignItems="flex-start"
                spacing={0.5}
                >
                    <FormTextFiled name="ASSET" control={control} label="ASSET" sx={{width:200}}   />
                    <Button variant="contained" color="success"  onClick={handleSubmit(fnSearch)}>조회</Button>
            </Stack>
            <TuiGrid columns={columns} ref={childRef}   />
            <TuiGrid columns={columns_transfer} ref={childTransferRef}   />
            </>
    )
  }
