import Grid,{GridHandler,Maybe} from '@/form-components/Grid';
import GridCombo from '@/form-components/GridCombo';
import GridTextEditor from '@/form-components/GridTextEditor';
import { useForm } from "react-hook-form";
import {FormSelect}  from '@/form-components/FormSelect';
import {FormTextFiled} from '@/form-components/FormTextFiled';
import React, { useState,useEffect,useRef,useReducer,useContext } from "react";
import send from '@/utils/send';
import {Stack,Button} from '@mui/material'
import {MenuContext} from "@/store/MenuStore";
import { CalculatedColumn } from 'react-data-grid';


interface IPageProps {
    onRowDoubleClick?: Maybe<(row: any, column: CalculatedColumn<any, any>) => void>;
}


interface IFormInput {
    CATEGORY: string;
  }

const defaultValues = {
    CATEGORY: "",
};


export  const CM_1150__프로그램조회팝업 = (props:IPageProps) => {   
    const menuStoreContext = useContext(MenuContext)
    const {messageAlert,messageConfirm,inlineDialog } = menuStoreContext;

    const childRef = useRef<GridHandler>(null);
    const [ctgData,setCtgData] = useState([]);
    const methods = useForm<IFormInput>({ defaultValues: defaultValues});
    const { handleSubmit, reset, control,formState: { errors } ,watch,setError } = methods;
    const fnSearch= async (data: IFormInput) => {
        if(childRef.current){
            childRef.current.loadData({
                                        BR:"BR_CM_PGM_FIND",
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
            const data:any= await send("BR_CM_CD_FIND", {brRq: 'IN_DATA'
            ,brRs: 'OUT_DATA'
            ,IN_DATA: [  { GRP_CD : 'CATEGORY',  USE_YN: 'Y'}]
            });

            const tmp= data.OUT_DATA.map((m:any)=>{
                return {
                    value: m["CD"],
                    text: m["CD_NM"]            
                };
            })
            setCtgData(tmp)
        }
        getData();
        handleSubmit(fnSearch)()
    },[]);

    const columns = [
        { key: 'PGM_NO', name: 'PGM_NO', width: 100},
        { key: "PGM_ID", name: '프로그램', width: 200 ,  sortable: true},
        { key: "PGM_NM", name: '프로그램', width: 200 ,  sortable: true},
        { key: 'CATEGORY', name: '프로그램명', width: 100 ,  sortable: true 
                ,editor: (p:any) =>{
                    //console.log(p)
                    return (                    
                        <GridCombo p={p} options={ctgData}  />
                      )
                }
        },
    ];

    const gridOnRowDoubleClickHandler  = (row: any, column: CalculatedColumn<any, any>)=>{
        console.log(row);       
        //fnSearch(row.GRP_CD);
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
                    <FormSelect name="CATEGORY" control={control} label="카테고리" sx={{width:200}} rules={{ required: false }}  options={ctgData}  />
                    <FormTextFiled name="SEARCH_NM" control={control} label="검색어" sx={{width:200}}  rules={{ required: false }}   />
                    <Button variant="contained" color="success"   onClick={handleSubmit(fnSearch)}>조회</Button>
            </Stack>
            <Grid style={{ height: 600, width: '100%' }} columns={columns}  checkbox={true} showRowStatus={true} ref={childRef} onRowDoubleClick={props.onRowDoubleClick} />
            </>
    )
  }
  
  

