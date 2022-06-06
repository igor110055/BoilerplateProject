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
    ASSET:"",
};

export const BINANCE_0178__자산상세= () => {   
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
        ,{header: 'MIN_WITHDRAW_AMOUNT',name: 'MIN_WITHDRAW_AMOUNT',width: 200,align : 'left',sortable : true,resizable: true,sortingType: 'desc'}
        ,{header: 'DEPOSIT_STATUS',name: 'DEPOSIT_STATUS',width: 200,align : 'left',sortable : true,resizable: true,sortingType: 'desc'}
        ,{header: 'WITHDRAW_FEE',name: 'WITHDRAW_FEE',width: 200,align : 'left',sortable : true,resizable: true,sortingType: 'desc'}
        ,{header: 'WITHDRAW_STATUS',name: 'WITHDRAW_STATUS',width: 200,align : 'left',sortable : true,resizable: true,sortingType: 'desc'}
        ,{header: 'DEPOSIT_TIP',name: 'DEPOSIT_TIP',width: 200,align : 'left',sortable : true,resizable: true,sortingType: 'desc'}
    ];

    const fnSearch= async (data: IFormInput) => {
        
        send("BR_BINANCE_WALLET_GET_SAPI_V1_AssetDetail", {
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
            <h3>Asset Detail (USER_DATA)</h3>
            GET /sapi/v1/asset/assetDetail (HMAC SHA256)
            Fetch details of assets supported on Binance.
            <b>Weight(IP):</b>  1
            </pre>
            <pre className="tal lh12">
            Please get network and other deposit or withdraw details from GET /sapi/v1/capital/config/getall.
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
            </>
    )
  }
