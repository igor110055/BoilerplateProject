import GridCombo from '@/form-components/GridCombo';
import GridTextEditor from '@/form-components/GridTextEditor';
import { useForm } from "react-hook-form";
import {FormSelect}  from '@/form-components/FormSelect';
import {FormTextFiled} from '@/form-components/FormTextFiled';
import React, { useState,useEffect,useRef,useReducer,useContext } from "react";
import send from '@/utils/send';
import {Stack,Button} from '@mui/material'
import {MenuContext} from "@/store/MenuStore";
import Grid,{GridHandler,Maybe} from '@/form-components/Grid';
import { CalculatedColumn } from 'react-data-grid';
import { IPageProps } from './CM_6200__게시판';


interface IFormInput {
    GRP_CD: string;
  }

const defaultValues = {
    GRP_CD: ""
};

export  const CM_6200__게시판_list = (props:IPageProps) => {   
    const menuStoreContext = useContext(MenuContext)
    const {messageAlert,messageConfirm } = menuStoreContext;

    const childRef = useRef<GridHandler>(null);
    const [ctgData,setCtgData] = useState([]);
    const methods = useForm<IFormInput>({ defaultValues: defaultValues});
    const { handleSubmit, reset, control,formState: { errors } ,watch,setError } = methods;
    const fnSearch= async (data: IFormInput) => {
        if(childRef.current){
            childRef.current.loadData({
                                        BR:"BR_CM_BOARD_FIND",
                                        PARAM: {
                                            brRq : 'IN_DATA'
                                            ,brRs : 'OUT_DATA'
                                            ,IN_DATA:[data]
                                        }
                                    });

        }
    }
    useEffect(()=>{
        const getData = async ()=>{
            const data:any= await send("BR_CM_BOARD_GROUP_FIND", {brRq: 'IN_DATA'
            ,brRs: 'OUT_DATA'
            ,IN_DATA: [{}]
            });

            const tmp= data.OUT_DATA.map((m:any)=>{
                return {
                    value: m["GRP_CD"],
                    text: m["GRP_NM"]            
                };
            })
            setCtgData(tmp)
        }
        getData();
        handleSubmit(fnSearch)()
    },[]);

    const columns = [
        { key: 'GRP_CD', name: '관리코드', width: 110 ,  sortable: true, dataType:"number"  },
        { key: 'GRP_NM', name: '게시판선택', width: 100 },
        { key: "BRD_SEQ", name: 'BRD_SEQ', width: 80 ,  sortable: true },
        { key: 'BRD_NO', name: '게시판번호', width: 100 ,  sortable: true  },
        { key: 'BRD_RPLY_ORD', name: '게시판답글순서', width: 120,  sortable: true  },
        { key: 'BRD_DPTH', name: '답글깊이', width: 80 ,  sortable: true  },
        { key: 'REF_BRD_SEQ', name: '참조일련번호', width: 110 ,  sortable: true  },
        { key: 'TTL_TEXT', name: '제목', width: 200 ,  sortable: true  },
        { key: 'DEL_YN', name: '삭제여부', width: 80 ,  sortable: true  },
        { key: 'CRT_DTM', name: '생성일', width: 160 },
        { key: 'UPDT_DTM', name: '수정일', width: 160 },
    ];
    
    const gridOnRowClickHandler  = (row: any, column: CalculatedColumn<any, any>)=>{
        console.log(props)
        props.setMode("view")
        props.setBrdSeq!(row.BRD_SEQ)
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
                    <FormSelect name="GRP_SEQ" control={control} label="게시판" sx={{width:200}} rules={{ required: false }}  options={ctgData}  />
                    <Button variant="contained" color="success"   onClick={handleSubmit(fnSearch)}>조회</Button>
                    <Button variant="contained" color="success" onClick={() => {
                                                                            props.setMode("new");
                                                                        }}>추가</Button>                                                                 
            </Stack>
            <Grid style={{ height: 600, width: '100%' }} columns={columns}  checkbox={false} showRowStatus={false} ref={childRef} onRowClick={gridOnRowClickHandler}  />
            </>
    )
  }
  
  

