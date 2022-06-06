import TuiGrid,{TuiGridHandler} from '@/form-components/TuiGrid';
import { Button, Stack } from '@mui/material';
import { useState,useEffect, useRef, useContext } from 'react';
import { OptColumn, OptHeader } from 'tui-grid/types/options';
import {commaRenderer} from '@/form-components/tui-grid-renderer/commaRenderer'
import {commaStRenderer} from '@/form-components/tui-grid-renderer/commaStRenderer'
import {buttonRenderer} from '@/form-components/tui-grid-renderer/buttonRenderer'
import {datetimeRenderer} from '@/form-components/tui-grid-renderer/datetimeRenderer'
import { useForm } from 'react-hook-form';
import { FormTextFiled } from '@/form-components/FormTextFiled';
import { FormSelect } from '@/form-components/FormSelect';
import { MenuContext } from '@/store/MenuStore';
import send from '@/utils/send';


interface IFormInput {
    SYMBOL:string;
}

const defaultValues = {
    SYMBOL:"",
};




export const FX_0160__업비트주문_sub2= () => {   
    const menuStoreContext = useContext(MenuContext)
    const {messageAlert,messageConfirm } = menuStoreContext;
    
    const methods = useForm<IFormInput>({ defaultValues: defaultValues});
    const { handleSubmit, reset, control,formState: { errors } ,watch,setError,setValue } = methods;
    const childRef = useRef<TuiGridHandler>(null);
    const [side,setSide] = useState([]);
        
    useEffect(()=>{
        const getData = async ()=>{
            const data:any= await send("BR_CM_CD_FIND", {brRq: 'IN_DATA'
            ,brRs: 'OUT_DATA'
            ,IN_DATA: [  { GRP_CD : 'UPBIT_SIDE_CD',  USE_YN: 'Y'}]
            });

            const tmp= data.OUT_DATA.map((m:any)=>{
                return {
                    value: m["CD"],
                    text: m["CD_NM"]            
                };
            })
            setSide(tmp)
        }
        getData();
        handleSubmit(fnSearch)()
    },[]);

    
    const fnSearch= async (data: IFormInput) => {
        if(childRef.current){
            childRef.current.loadData({
                                        BR:"BR_FX_UPBIT_ORDER_retrieveUpbitOrderOutLog",
                                        PARAM: {
                                            brRq : 'IN_PSET'
                                            ,brRs : 'OUT_RSET'
                                            ,IN_PSET:[data]
                                        }
                                    });

        }
        
    }


      const columns:OptColumn[] = [ 
        {header : 'IDENTIFIER',name : 'IDENTIFIER',width : 100,sortable : true,align : "center"}
        ,{header : 'SEQ_UUID',name : 'SEQ_UUID',width : 100,resizable : false}
        ,{header : 'MARKET',name : 'MARKET',width : 100,align : "left"}
        ,{header : 'SIDE',name : 'SIDE',width : 30,resizable : false,align : "center"}
        ,{header : 'ORD_TYPE',name : 'ORD_TYPE',width : 70,align : "center"}
        ,{header : 'STATE',name : 'STATE',width : 40,align : "center"}
        ,{header : 'PRICE',name : 'PRICE',width : 80,resizable : false,align : "right"}
        ,{header : 'AVG_PRICE',name : 'AVG_PRICE',width : 80,resizable : false,align : "right"}
        ,{header : 'VOLUME',name : 'VOLUME',width : 80,align : "right"}
        ,{header : 'REMAINING_VOLUME',name : 'REMAINING_VOLUME',width : 130,align : "right"}
        ,{header : 'RESERVED_FEE',name : 'RESERVED_FEE',width : 100,align : "right"}
        ,{header : 'REMAINING_FEE',name : 'REMAINING_FEE',width : 100,align : "right"}
        ,{header : 'PAID_FEE',name : 'PAID_FEE',width : 70,align : "right"}
        ,{header : 'LOCKED',name : 'LOCKED',width : 80,align : "right"}
        ,{header : 'EXECUTED_VOLUME',name : 'EXECUTED_VOLUME',width : 120,align : "right"}
        ,{header : 'TRADE_COUNT',name : 'TRADE_COUNT',width : 100,align : "right"}
        ,{header : 'CREATED_AT',name : 'CREATED_AT',width : 120,align : "center"}
        ,{header : '생성일',name : 'CRT_DTM',width : 140,sortable : true,align : "center"} 
    ];

    return (
            <>
            <Stack
                mt={2}
                direction="row"
                justifyContent="flex-start"
                alignItems="flex-start"
                spacing={0.5}
                >
                    <FormTextFiled name="IDENTIFIER" control={control} label="IDENTIFIER" sx={{width:200}}  rules={{ required: false }}   />
                    <FormTextFiled name="MARKET" control={control} label="MARKET" sx={{width:200}}  rules={{ required: false }}   />
                    <FormSelect name="SIDE" control={control} label="SIDE" sx={{width:200}} rules={{ required: false }} options={side}  />
                    <Button variant="contained" color="success"   onClick={handleSubmit(fnSearch)}>조회</Button>
            </Stack>
            <TuiGrid   columns={columns}   ref={childRef} />
            </>
    )
  }
function messageAlert(arg0: string) {
    throw new Error('Function not implemented.');
}

