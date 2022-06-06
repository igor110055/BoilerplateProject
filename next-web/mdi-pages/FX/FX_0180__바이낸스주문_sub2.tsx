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
    IDENTIFIER:string;
    MARKET:string;
    SIDE:string;
}

const defaultValues = {
    IDENTIFIER:"",
    MARKET:"",
    SIDE:"",
};




export const FX_0180__바이낸스주문_sub2= () => {   
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
                                        BR:"BR_FX_BINANCE_ORDERED_retrieveBinanceOrderedFillsLog",
                                        PARAM: {
                                            brRq : 'IN_PSET'
                                            ,brRs : 'OUT_RSET'
                                            ,IN_PSET:[data]
                                        }
                                    });

        }
        
    }


      const columns:OptColumn[] = [ 
        {header : 'SYMBOL',name : 'SYMBOL',width : 100,sortable : false,align : "center"}
        ,{header : 'SIDE',name : 'SIDE',width : 80,resizable : false}
        ,{header : 'ORDER_ID',name : 'ORDER_ID',width : 80,resizable : false}
        ,{header : 'ORDER_LIST_ID',name : 'ORDER_LIST_ID',width : 100,align : "left"}
        ,{header : 'CLIENT_ORDER_ID',name : 'CLIENT_ORDER_ID',width : 110,resizable : false,align : "center"}
        ,{header : 'PRICE',name : 'PRICE',width : 80,align : "center"}
        ,{header : 'QTY',name : 'QTY',width : 70,align : "center"}
        ,{header : 'COMMISSION',name : 'COMMISSION',width : 90,resizable : false,align : "right"}
        ,{header : 'COMMISSION_ASSET',name : 'COMMISSION_ASSET',width : 130,resizable : false,align : "center"}
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
                    <FormTextFiled name="CLIENT_ORDER_ID" control={control} label="CLIENT_ORDER_ID" sx={{width:200}}  rules={{ required: false }}   />
                    <FormTextFiled name="SYMBOL" control={control} label="SYMBOL" sx={{width:200}}  rules={{ required: false }}   />
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

