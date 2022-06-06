import Grid,{GridHandler,Maybe} from '@/form-components/Grid';
import { useForm } from "react-hook-form";
import React, { useState,useEffect,useRef,useReducer,useContext } from "react";
import {Stack,Button} from '@mui/material'
import {MenuContext} from "@/store/MenuStore";
import { CalculatedColumn } from 'react-data-grid';

interface IPageProps {
    onRowDoubleClick?: Maybe<(row: any, column: CalculatedColumn<any, any>) => void>;
}

interface IFormInput {
}

const defaultValues = {
};

export  const CM_2300__사용자조회 = (props:IPageProps) => {   
    const menuStoreContext = useContext(MenuContext)
    const {messageAlert,messageConfirm,inlineDialog } = menuStoreContext;
    const childRef = useRef<GridHandler>(null);
    const methods = useForm<IFormInput>({ defaultValues: defaultValues});
    const { handleSubmit, reset, control,formState: { errors } ,watch,setError } = methods;
    const fnSearch= async (data: IFormInput) => {
        if(childRef.current){
            childRef.current.loadData({
                                        BR:"BR_CM_USER_FIND",
                                        PARAM: {
                                            brRq : 'IN_DATA'
                                            ,brRs : 'OUT_DATA'
                                            ,IN_DATA:[data]
                                        }
                                    });

        }
    }
    useEffect(()=>{
        handleSubmit(fnSearch)()
    },[]);

    const columns = [
        { key: 'USER_NO', name: '사용자번호', width: 100},
        { key: "USER_NM", name: '사용자명', width: 200 ,  sortable: true},
        { key: "USER_ID", name: '사용자ID', width: 200 ,  sortable: true},
        { key: 'USE_YN', name: '사용여부', width: 100 ,  sortable: true },
        { key: "RMK", name: '비고', width: 200 ,  sortable: true},
        { key: "LST_ACC_DTM", name: '마지막접속일', width: 200 ,  sortable: true},
        { key: 'CRT_DTM', name: '생성일', width: 160 },
        { key: 'UPDT_DTM', name: '수정일', width: 160 },
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
            <Grid style={{ height: 600, width: '100%' }} columns={columns}  checkbox={false} showRowStatus={true} ref={childRef} onRowDoubleClick={props.onRowDoubleClick} />
            </>
    )
  }