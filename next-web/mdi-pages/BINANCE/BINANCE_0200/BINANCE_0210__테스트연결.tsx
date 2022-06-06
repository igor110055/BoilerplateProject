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

export const BINANCE_0210__테스트연결= () => {   
    const menuStoreContext = useContext(MenuContext)
    const {messageAlert,messageConfirm,inlineDialog,inlineDialogClose } = menuStoreContext;
    
     const fnPingHandler=(event: React.MouseEvent<HTMLButtonElement>) => {
        send('BR_BINANCE_MARKET_GET_API_V3_PING',{
            brRq 		: ''
            ,brRs 		: 'OUT_RSET,OUT_RST'
        }).then(function(data:any){
            //_this.hideProgress();
            if(data){
                console.log(data);
				messageAlert(data.OUT_RST[0].STATUS);
            }
        })                      
    }

    const fnChkServerTimeHandler=(event: React.MouseEvent<HTMLButtonElement>) => {
        send('BR_BINANCE_MARKET_GET_API_V3_TIME',{
            brRq 		: ''
            ,brRs 		: 'OUT_RSET,OUT_RST'
        }).then(function(data:any){
            //_this.hideProgress();
            if(data){
                console.log(data);
                messageAlert(data.OUT_RSET[0].SERVER_TIME,function()  {
                    
                });
            }
        })                      
    }


    return (
            <>
            <pre className="tal lh12">
            <h3>Test Connectivity</h3>
            GET /api/v3/ping
            Test connectivity to the Rest API.
            <b>Weight(IP):</b> 1
            </pre>    
            <hr />
            <pre className="tal lh12">
            <h3>Check Server Time</h3>
            GET /api/v3/time
            Test connectivity to the Rest API and get the current server time.
            <b>Weight(IP):</b> 1
            </pre>        
            <Stack
                mt={2}
                direction="row"
                justifyContent="flex-start"
                alignItems="flex-start"
                spacing={0.5}
                >
                <Button variant="contained" color="success"  onClick={fnPingHandler}>핑</Button>
                <Button variant="contained" color="success"  onClick={fnChkServerTimeHandler}>서버시간</Button>
            </Stack>
            </>
    )
  }
