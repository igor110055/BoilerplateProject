import TuiGrid,{TuiGridHandler} from '@/form-components/TuiGrid';
import { Button, Stack } from '@mui/material';
import { useState,useEffect, useRef, useContext } from 'react';
import { OptColumn, OptHeader } from 'tui-grid/types/options';
import {commaRenderer} from '@/form-components/tui-grid-renderer/commaRenderer'
import {commaStRenderer} from '@/form-components/tui-grid-renderer/commaStRenderer'
import {buttonRenderer} from '@/form-components/tui-grid-renderer/buttonRenderer'
import {datetimeRenderer} from '@/form-components/tui-grid-renderer/datetimeRenderer'
import {commaRendererRemoveDot} from '@/form-components/tui-grid-renderer/commaRendererRemoveDot'


import { useForm } from 'react-hook-form';
import { FormTextFiled } from '@/form-components/FormTextFiled';
import { FormSelect } from '@/form-components/FormSelect';
import { MenuContext } from '@/store/MenuStore';
import send from '@/utils/send';

import * as pjtUtil from '@/utils/pjtUtil'


interface IFormInput {
    CURRENCY:string;
    AL_UUIDS:string;
    AL_TXIDS:string;
    STATE:string;
}

const defaultValues = {
    CURRENCY:"",
    AL_UUIDS:"",
    AL_TXIDS:"",
    STATE:"",
};

export const UPBIT_0260__업비트전체입금주소조회= () => {   
    const menuStoreContext = useContext(MenuContext)
    const {messageAlert,messageConfirm } = menuStoreContext;
    
    const methods = useForm<IFormInput>({ defaultValues: defaultValues});
    const { handleSubmit, reset, control,formState: { errors } ,watch,setError,setValue } = methods;
    const childRef = useRef<TuiGridHandler>(null);
        
    useEffect(()=>{
        handleSubmit(fnSearch)()
    },[]);
    
    const fnSearch= async (data: IFormInput) => {
        console.log({data})

        if(childRef.current){
            childRef.current.loadData({
                                        BR:"BR_UPBIT_EXCHANGE_GET_DEPOSITS_COIN_ADDRESSES",
                                        PARAM: {
                                            brRq : 'IN_DATA'
                                            ,brRs : 'OUT_DATA'
                                            ,IN_DATA:[  ]
                                        }
                                    });
        }
    }

      const columns:OptColumn[] = [ 
        {header: '화폐를 의미하는 영문 대문자 코드',name: 'CURRENCY',width: 100,align : 'left',filter : {type : 'text',showApplyBtn : true,showClearBtn : true},resizable: true,sortable : true,sortingType: 'desc'}
        ,{header: '입금 주소',name: 'DEPOSIT_ADDRESS',width: 400,align : 'left',sortable : true,resizable: true,sortingType: 'desc'}
        ,{header: '2차 입금 주소',name: 'SECONDARY_ADDRESS',width: 300,align : 'left',sortable : true,resizable: true,sortingType: 'desc'}
    ];

     
    const saveHandler= async (data: IFormInput) => {
        if(childRef.current){          
            messageConfirm("원화입금을 하시겠습니까?",function()  {
                //_this.showProgress();	
                send('BR_UPBIT_EXCHANGE_POST_DEPOSITS_KRW',{
                    brRq : 'IN_DATA'
                    ,brRs : 'OUT_DATA'
                    ,IN_DATA:[ data ]
                }).then(function(data2){
                    //_this.hideProgress();
                    if(data2){
                        messageAlert("원화입금을 하였습니다.",function()  {
                            handleSubmit(fnSearch)()
                        });
                    }
                })                      
            })
        }
    }

    return (
            <>
            <Stack
                mt={2}
                direction="row"
                justifyContent="flex-start"
                alignItems="flex-start"
                spacing={0.5}
                >
                    <FormTextFiled name="AMOUNT" control={control} label="입금 원화 수량" sx={{width:200}}  rules={{ required: false }}   />
                    <Button variant="contained" color="success"   onClick={handleSubmit(fnSearch)}>조회</Button>
                    <Button variant="contained" color="success"  onClick={handleSubmit(saveHandler)}>원화입금하기</Button>                              
            </Stack>
            <TuiGrid   columns={columns}   ref={childRef} />
            </>
    )
  }
