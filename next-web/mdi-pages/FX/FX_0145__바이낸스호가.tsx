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




export const FX_0145__바이낸스호가= () => {   
    const menuStoreContext = useContext(MenuContext)
    const {messageAlert,messageConfirm } = menuStoreContext;
    
    const methods = useForm<IFormInput>({ defaultValues: defaultValues});
    const { handleSubmit, reset, control,formState: { errors } ,watch,setError,setValue } = methods;
    const childRef = useRef<TuiGridHandler>(null);
        
    useEffect(()=>{
        handleSubmit(fnSearch)()
    },[]);

    
    const fnSearch= async (data: IFormInput) => {
        if(childRef.current){
            childRef.current.loadData({
                                        BR:"BR_FX_retrieveTmpBinanceHoga",
                                        PARAM: {
                                            brRq : 'IN_PSET'
                                            ,brRs : 'OUT_RSET'
                                            ,IN_PSET:[data]
                                        }
                                    });

        }
        
    }


      const columns:OptColumn[] = [ 
        {header: 'FX_SEQ',name: 'FX_SEQ',width: 100,align : 'center',sortable : false,resizable: true,sortingType: 'desc'}
        ,{header: 'SYMBOL',name: 'SYMBOL',width: 100,align : 'left',sortable : false,resizable: true,sortingType: 'desc'}
        ,{header: 'SEQ',name: 'SEQ',width: 30,align : 'right',sortable : false,resizable: true,sortingType: 'desc'}
        ,{header: 'ASK_PRICE',name: 'ASK_PRICE',width: 100,align : 'right',sortable : false,resizable: true,sortingType: 'desc',renderer : {type: commaRenderer}}
        ,{header: 'BID_PRICE',name: 'BID_PRICE',width: 100,align : 'right',sortable : false,resizable: true,sortingType: 'desc',renderer : {type: commaRenderer}}
        ,{header: 'ASK_QTY',name: 'ASK_QTY',width: 240,align : 'right',sortable : false,resizable: true,sortingType: 'desc',renderer : {type: commaRenderer}}
        ,{header: 'BID_QTY',name: 'BID_QTY',width: 240,align : 'right',sortable : false,resizable: true,sortingType: 'desc',renderer : {type: commaRenderer}}
        ,{header: 'TRADE_TYPE',name: 'TRADE_TYPE',width: 100,align : 'right',sortable : false,resizable: true,sortingType: 'desc'}
        ,{header : '생성일',name : 'CRT_DTM',width : 120,sortable : false,align : "center",renderer : {type : datetimeRenderer,options : {format : 'yyyy-MM-DD HH:mm' /*YYYYMMDDHHmmss    이게 풀양식이다.*/,source : 'YYYYMMDDHHmmss' /*TIME 초, YYYYMMDD , YYYYMMDDHHmm,  YYYYMMDDHHmmss  */}}}
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
                    <FormTextFiled name="SYMBOL" control={control} label="SYMBOL" sx={{width:200}}  rules={{ required: false }}   />
                    <Button variant="contained" color="success"   onClick={handleSubmit(fnSearch)}>조회</Button>
            </Stack>
            <TuiGrid   columns={columns}   ref={childRef} />
            </>
    )
  }
function messageAlert(arg0: string) {
    throw new Error('Function not implemented.');
}

