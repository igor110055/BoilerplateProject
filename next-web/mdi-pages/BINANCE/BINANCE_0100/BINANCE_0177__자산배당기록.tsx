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
    START_TIME:string;
    END_TIME:string;
    LIMIT:string;
}

const defaultValues = {
    ASSET:"",
    START_TIME:"",
    END_TIME:"",
    LIMIT:""

};

export const BINANCE_0177__자산배당기록= () => {   
    const menuStoreContext = useContext(MenuContext)
    const {messageAlert,messageConfirm,inlineDialog,inlineDialogClose } = menuStoreContext;
    const childRef = useRef<GridHandler>(null);
       
    useEffect(()=>{

    },[])

    const [rows,setRows] = useState([]);
    const methods = useForm<IFormInput>({ defaultValues: defaultValues});
    const { handleSubmit, reset, control,formState: { errors } ,watch,setError } = methods;

  
    const columns:OptColumn[] = [ 
        {header: 'AMOUNT',name: 'AMOUNT',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc'}
        ,{header: 'ASSET',name: 'ASSET',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc'}
        ,{header: 'DIV_TIME',name: 'DIV_TIME',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc'}
        ,{header: 'EN_INFO',name: 'EN_INFO',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc'}
        ,{header: 'TRAN_ID',name: 'TRAN_ID',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc'}
    ];

    const fnSearch= async (data: IFormInput) => {
        
        send("BR_BINANCE_WALLET_GET_SAPI_V1_AssetDividendRecord", {
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
            <h3>Asset Dividend Record (USER_DATA)</h3>
            GET /sapi/v1/asset/assetDividend (HMAC SHA256)
            Query asset dividend record.
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
                    <FormTextFiled name="START_TIME" control={control} label="START_TIME" sx={{width:200}}    />
                    <FormTextFiled name="END_TIME" control={control} label="END_TIME" sx={{width:200}}   />
                    <FormTextFiled name="LIMIT" control={control} label="LIMIT" sx={{width:200}}   />
                    <Button variant="contained" color="success"  onClick={handleSubmit(fnSearch)}>조회</Button>
            </Stack>
            <TuiGrid columns={columns} ref={childRef}   />
            </>
    )
  }
