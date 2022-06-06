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

export const UPBIT_0275__업비트API키리스트조회= () => {   
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
                                        BR:"BR_UPBIT_EXCHANGE_GET_API_KEYS",
                                        PARAM: {
                                            brRq : 'IN_DATA'
                                            ,brRs : 'OUT_DATA'
                                            ,IN_DATA:[  ]
                                        }
                                    });
        }
    }

      const columns:OptColumn[] = [ 
		{
            header: 'ACCESS_KEY',
            name: 'ACCESS_KEY',
            width: 300,
            align : 'left',
            sortable : true,
            resizable: true,
            sortingType: 'desc'
         },{
            header: 'EXPIRE_AT',
            name: 'EXPIRE_AT',
            width: 300,
            align : 'left',
            sortable : true,
            resizable: true,
            sortingType: 'desc'
         }
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
                    <Button variant="contained" color="success"   onClick={handleSubmit(fnSearch)}>조회</Button>
            </Stack>
            <TuiGrid   columns={columns}   ref={childRef} />
            </>
    )
  }
