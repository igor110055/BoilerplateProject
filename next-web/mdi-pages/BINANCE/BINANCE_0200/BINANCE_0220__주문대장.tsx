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

export const BINANCE_0220__주문대장= () => {   
    const menuStoreContext = useContext(MenuContext)
    const {messageAlert,messageConfirm } = menuStoreContext;
    
    const methods = useForm<IFormInput>({ defaultValues: defaultValues});
    const { handleSubmit, reset, control,formState: { errors } ,watch,setError,setValue } = methods;
    const childRef_bid = useRef<TuiGridHandler>(null);
    const childRef_ask = useRef<TuiGridHandler>(null);
    
    const fnSearch= async (data: IFormInput) => {
        console.log({data})

        if(childRef_bid.current){
            childRef_bid.current.loadData({
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
            <pre className="tal lh12">
            Limit	                 Weight
            5, 10, 20, 50, 100	   1
            500	                   5
            1000	                 10
            5000	                 50
            Parameters:

            Name	   Type	     Mandatory	Description
            symbol	 STRING	   YES	
            limit	   INT	     NO	        Default 100; max 5000. Valid limits:[5, 10, 20, 50, 100, 500, 1000, 5000]

            Data Source: Memory
            </pre>   
            <Stack
                mt={2}
                direction="row"
                justifyContent="flex-start"
                alignItems="flex-start"
                spacing={0.5}
                >
                    <FormTextFiled name="SYMBOL" control={control} label="SYMBOL(필수)" sx={{width:200}}  rules={{ required: false }}   />
                    <Button variant="contained" color="success"   onClick={handleSubmit(fnSearch)}>조회</Button>
            </Stack>
            <table>
            <tr>
                <td>매수(BID)</td>
                <td>매도(ASK)</td>
            </tr>
            <tr>
                <td><TuiGrid   columns={
                    [
                        {header: 'SYMBOL',name: 'SYMBOL',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc',filter : {type : 'text',showApplyBtn : true,showClearBtn : true} /*text, number, select, date 4가지가 있다.*/}
                        ,{header: 'BID_PRICE',name: 'BID_PRICE',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc',filter : {type : 'text',showApplyBtn : true,showClearBtn : true} /*text, number, select, date 4가지가 있다.*/}
                        ,{header: 'BID_QTY',name: 'BID_QTY',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc',filter : {type : 'text',showApplyBtn : true,showClearBtn : true} /*text, number, select, date 4가지가 있다.*/}
                        ,{header: 'SEQ',name: 'SEQ',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc',filter : {type : 'text',showApplyBtn : true,showClearBtn : true} /*text, number, select, date 4가지가 있다.*/}
                    ]}   ref={childRef_bid} /></td>
                <td><TuiGrid   columns={
                    [
                        {header: 'SYMBOL',name: 'SYMBOL',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc',filter : {type : 'text',showApplyBtn : true,showClearBtn : true} /*text, number, select, date 4가지가 있다.*/}
                        ,{header: 'ASK_PRICE',name: 'ASK_PRICE',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc',filter : {type : 'text',showApplyBtn : true,showClearBtn : true} /*text, number, select, date 4가지가 있다.*/}
                        ,{header: 'ASK_QTY',name: 'ASK_QTY',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc',filter : {type : 'text',showApplyBtn : true,showClearBtn : true} /*text, number, select, date 4가지가 있다.*/}
                        ,{header: 'SEQ',name: 'SEQ',width: 100,align : 'left',sortable : true,resizable: true,sortingType: 'desc',filter : {type : 'text',showApplyBtn : true,showClearBtn : true} /*text, number, select, date 4가지가 있다.*/}
                    ]
                    }   ref={childRef_ask} /></td>
            </tr>
            </table>
            </>
    )
}
