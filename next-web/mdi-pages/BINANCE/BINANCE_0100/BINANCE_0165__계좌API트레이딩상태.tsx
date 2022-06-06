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


export const BINANCE_0165__계좌API트레이딩상태= () => {   
    const menuStoreContext = useContext(MenuContext)
    const {messageAlert,messageConfirm,inlineDialog,inlineDialogClose } = menuStoreContext;
    const childRef = useRef<GridHandler>(null);
    const childIndeicatorsRef = useRef<GridHandler>(null);
    
    useEffect(()=>{

    },[])

    const [rows,setRows] = useState([]);
    const methods = useForm<IFormInput>({ defaultValues: defaultValues});
    const { handleSubmit, reset, control,formState: { errors } ,watch,setError } = methods;

  
    const columns:OptColumn[] = [ 
        {header: 'IS_LOCKED',name: 'IS_LOCKED',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc'}
        ,{header: 'PLANNED_RECOVER_TIME',name: 'PLANNED_RECOVER_TIME',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc'}
        ,{header: 'TRIGGER_CONDITION__GCR',name: 'TRIGGER_CONDITION__GCR',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc'}
        ,{header: 'TRIGGER_CONDITION__IFER',name: 'TRIGGER_CONDITION__IFER',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc'}
        ,{header: 'TRIGGER_CONDITION__UFR',name: 'TRIGGER_CONDITION__UFR',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc'}
        ,{header: 'UPDATE_TIME',name: 'UPDATE_TIME',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc'}
    ];



    const columns_indicators:OptColumn[] = [ 
        {header: 'SYMBOL',name: 'SYMBOL',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc'}
        ,{header: 'UFR_INDICATOR',name: 'UFR_INDICATOR',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc'}
        ,{header: 'UFR_COUNT',name: 'UFR_COUNT',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc'}
        ,{header: 'UFR_CURRENT',name: 'UFR_CURRENT',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc'}
        ,{header: 'UFR_TRIGGER',name: 'UFR_TRIGGER',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc'}
        ,{header: 'IFER_INDICATOR',name: 'IFER_INDICATOR',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc'}
        ,{header: 'IFER_COUNT',name: 'IFER_COUNT',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc'	}
        ,{header: 'IFER_CURRENT',name: 'IFER_CURRENT',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc'}
        ,{header: 'IFER_TRIGGER',name: 'IFER_TRIGGER',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc'	}
        ,{header: 'GCR_INDICATOR',name: 'GCR_INDICATOR',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc'}
        ,{header: 'GCR_COUNT',name: 'GCR_COUNT',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc'}
        ,{header: 'GCR_CURRENT',name: 'GCR_CURRENT',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc'}
        ,{header: 'GCR_TRIGGER',name: 'GCR_TRIGGER',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc'}
    ];

    const fnSearch= async (data: IFormInput) => {
        
        send("BR_BINANCE_WALLET_GET_SAPI_V1_AccountApiTradingStatus", {
            brRq : ''
            ,brRs : 'OUT_RSET,OUT_RSET_INDICATORS'
        }).then(function(data:any){
            if(childRef.current){
                childRef.current.setData(data.OUT_RSET);
                if(childIndeicatorsRef.current){
                    childIndeicatorsRef.current.setData(data.OUT_RSET_INDICATORS);
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
            <h3>Account API Trading Status (USER_DATA)</h3>
            GET /sapi/v1/account/apiTradingStatus (HMAC SHA256)
            Fetch account api trading status detail.
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
            <TuiGrid columns={columns_indicators} ref={childIndeicatorsRef}   />
            </>
    )
  }
