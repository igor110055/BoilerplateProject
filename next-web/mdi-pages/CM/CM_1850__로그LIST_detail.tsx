import {Box,Stack,Button,Typography,Breadcrumbs,Link} from '@mui/material'
import { useForm } from "react-hook-form";
import send from '../../utils/send';
import React, { useState,useEffect,useRef,useReducer,useContext } from "react";
import {MenuContext} from "@/store/MenuStore";
import { PwdTextFiled } from "@/form-components/PwdTextFiled";
import { dark } from 'react-syntax-highlighter/dist/cjs/styles/prism';
import { Prism as SyntaxHighlighter } from 'react-syntax-highlighter';
import { countBy } from 'lodash';

export interface IProps {
    bizactorLogId?:string;
}

interface IFormInput {
    BIZACTOR_LOG_ID: string;
    SEQ: string;
    ACT_ID: string;
    IN_DT_NAME: string;
    OUT_DT_NAME: string;
    IN_JSON_STR: string;
    OUT_JSON_STR: string;
    IN_DS_STR: string;
    OUT_DS_STR: string;
    GAP: string;
    ERR_STR: string;
    CLIENT_IP: string;
    LOG_DATE: string;
    
}

const defaultValues = {
    BIZACTOR_LOG_ID:""
    ,SEQ:""
    ,ACT_ID:""
    ,IN_DT_NAME:""
    ,OUT_DT_NAME:""
    ,IN_JSON_STR:""
    ,OUT_JSON_STR:""
    ,IN_DS_STR:""
    ,OUT_DS_STR:""
    ,GAP:""
    ,ERR_STR:""
    ,CLIENT_IP:""
    ,LOG_DATE:""
};

export  const CM_1850__로그LIST_detail = (props:IProps) => {   
    const menuStoreContext = useContext(MenuContext)
    const {messageAlert,messageConfirm,inlineDialog,inlineDialogClose } = menuStoreContext;
    const password = useRef({});
    const [data,setData] = useState<IFormInput>(defaultValues)
    
    useEffect(()=>{
        const getData = async ()=>{
            const data:any= await send("BR_CM_API_LOG_GetApiLog", {brRq: 'IN_PSET'
            ,brRs: 'OUT_REST'
            ,IN_PSET: [{ BIZACTOR_LOG_ID : props.bizactorLogId} ]
            });
            console.log(data)
            if(data.OUT_REST.length > 0){
                setData(data.OUT_REST[0])
            }            
        }
        getData();
    },[]);

    
    const inJsonHandler =(event: React.MouseEvent<HTMLButtonElement>) => {
        event.preventDefault();

        let tmp =JSON.parse(data.IN_JSON_STR);        
        console.log(tmp)
        const txt=(
            <SyntaxHighlighter language="javascript" style={dark}>
                {JSON.stringify(tmp,null,2)}
            </SyntaxHighlighter>
        )
        inlineDialog(txt);
    }

    const outJsonHandler =(event: React.MouseEvent<HTMLButtonElement>) => {
        event.preventDefault();

        let tmp =JSON.parse(data.OUT_JSON_STR);        
        console.log(tmp)
        const txt=(
            <SyntaxHighlighter language="javascript" style={dark}>
                {JSON.stringify(tmp,null,2)}
            </SyntaxHighlighter>
        )
        inlineDialog(txt);
    }

    
    const inDsHandler =(event: React.MouseEvent<HTMLButtonElement>) => {
        event.preventDefault();
        const txt=(
            <SyntaxHighlighter language="xml" style={dark}>
                {data.IN_DS_STR}
            </SyntaxHighlighter>
        )
        inlineDialog(txt);
    }

    const outDsHandler =(event: React.MouseEvent<HTMLButtonElement>) => {
        event.preventDefault();
        const txt=(
            <SyntaxHighlighter language="xml" style={dark}>
                {data.OUT_DS_STR}
            </SyntaxHighlighter>
        )
        inlineDialog(txt);
    }


    
    return (
        <>
              <Box m={2}  sx={{width: 500,height: 300,border: 0 ,mx:"auto",justifyContent: 'center'}}>
              BIZACTOR_LOG_ID:{data.BIZACTOR_LOG_ID} <br />
              SEQ:{data.SEQ} <br />
              ACT_ID:{data.ACT_ID} <br />
              IN_DT_NAME:{data.IN_DT_NAME} <br />
              OUT_DT_NAME:{data.OUT_DT_NAME} <br />
              LOG_DATE:{data.LOG_DATE} <br />
              CLIENT_IP:{data.CLIENT_IP} <br />
              ERR_STR:{data.ERR_STR} <br />
              GAP:{data.GAP} <br />
              <Button variant="contained" color="success"  onClick={inJsonHandler}>IN_JSON_STR</Button>                  
              <Button variant="contained" color="success"  onClick={outJsonHandler}>OUT_JSON_STR</Button><br />
              <Button variant="contained" color="success"  onClick={inDsHandler}>IN_DS_STR</Button>           
              <Button variant="contained" color="success" onClick={outDsHandler}>OUT_DS_STR</Button>                  
                  
              </Box>
        </>
    )
  }
