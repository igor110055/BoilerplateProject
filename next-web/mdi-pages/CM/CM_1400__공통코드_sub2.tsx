import Grid,{GridHandler} from '@/form-components/Grid';
import GridCombo from '@/form-components/GridCombo';
import GridTextEditor from '@/form-components/GridTextEditor';
import { useForm } from "react-hook-form";
import {FormTextFiled} from '@/form-components/FormTextFiled';
import React, { useState,useEffect,useRef,useReducer,useContext, forwardRef ,useImperativeHandle} from "react";
import send,{ISend} from '@/utils/send';
import {Stack,Button} from '@mui/material'
import {MenuContext} from "@/store/MenuStore";
import { CalculatedColumn } from 'react-data-grid';

interface IFormInput {
    GRP_CD: string;
}
const defaultValues = {
    GRP_CD: "",
};

interface CM_1400__공통코드_sub2Props {
}
    

export type CM_1400__공통코드_sub2Handler = {
    fnSearchGrid: (GRP_CD:string)=>void;
    
};

export  const CM_1400__공통코드_sub2 = forwardRef<CM_1400__공통코드_sub2Handler, CM_1400__공통코드_sub2Props>((props, ref)=> {
    const menuStoreContext = useContext(MenuContext)
    const {messageAlert,messageConfirm } = menuStoreContext;
    
    const txtRef = useRef(null);
    const childGridRef = useRef<GridHandler>(null);
    const [useYnData,setUseYnData] = useState([]);
    const methods = useForm<IFormInput>({ defaultValues: defaultValues});
    const { handleSubmit, reset,setValue, register,control,formState: { errors } ,watch,setError } = methods;
    const fnSearch= async (data: IFormInput) => {
        if(childGridRef.current){
            childGridRef.current.loadData({
                                        BR:"BR_CM_CD_FIND",
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
            ,IN_DATA: [  { GRP_CD : 'USE_YN',  USE_YN: 'Y'}]
            });

            const tmp= data.OUT_DATA.map((m:any)=>{
                return {
                    value: m["CD"],
                    text: m["CD_NM"]            
                };
            })
            setUseYnData(tmp)
        }
        getData();
    },[]);

    
    useImperativeHandle(ref, () => ({
            fnSearchGrid(grp_cd:string) {
                console.log({grp_cd})
                setValue("GRP_CD",grp_cd)
                handleSubmit(fnSearch)()
            } 
      })
    );

    const columns = [
        { key: 'GRP_CD', name: '공통그룹코드', width: 220 ,editor: GridTextEditor},
        { key: "CD", name: '공통코드', width: 100 ,  sortable: true  ,editor: GridTextEditor},
        { key: "CD_NM", name: '공통코드명', width: 200 ,  sortable: true  ,editor: GridTextEditor},
        { key: 'USE_YN', name: '사용여부', width: 80 ,  sortable: true  ,editor: (p:any) =>(<GridCombo p={p} options={useYnData}  />) ,editorOptions: {editOnClick: true} },
        { key: 'ORD', name: '정렬', width: 80 ,editor: GridTextEditor},            
        { key: 'RMK', name: '비고', width: 100 ,editor: GridTextEditor},            
        { key: 'ATTR1', name: '부가속성1', width: 100 ,editor: GridTextEditor},            
        { key: 'ATTR2', name: '부가속성2', width: 100 ,editor: GridTextEditor},            
        { key: 'CRT_DTM', name: '생성일', width: 160 },
        { key: 'UPDT_DTM', name: '수정일', width: 160 },
    ];
    
    const saveHandler=(event: React.MouseEvent<HTMLButtonElement>) => {
        event.preventDefault();
        if(childGridRef.current){
            const data = childGridRef.current.getModifiedRows();
            var crt_cnt	= data.createdRows.length;
            var updt_cnt= data.updatedRows.length;
            if((crt_cnt+updt_cnt)==0) {
                messageAlert("공통코드 상세에 저장할 내용이 존재하지 않습니다.");
                return;
            }
            messageConfirm("공통코드상세를 저장하시겠습니까?",function()  {
                //_this.showProgress();	
                send('BR_CM_CD_SAVE',{
                    brRq 		: 'IN_DATA,UPDT_DATA,SESSION'
                    ,brRs 		: ''
                    ,IN_DATA	: data.createdRows
                    ,UPDT_DATA	: data.updatedRows
                }).then(function(data){
                    //_this.hideProgress();
                    if(data){
                        messageAlert("저장되었습니다",function()  {
                            handleSubmit(fnSearch)()
                        });
                    }
                })                      
            })
        }
    }

    const delHandler=(event: React.MouseEvent<HTMLButtonElement>) => {
        event.preventDefault();
        if(childGridRef.current){
            const data = childGridRef.current.getCheckedData();
            console.log({data});
            if(data.length<=0) {
                messageAlert('선택된 항목이 없습니다.');
                return;
            }
            messageConfirm("코드상세를 삭제하시겠습니까?",function()  {
                //_this.showProgress();	
                send('BR_CM_CD_RM',{
                    brRq 		: 'IN_DATA,SESSION'
                    ,brRs 		: ''
                    ,IN_DATA	: data
                }).then(function(data){
                    //_this.hideProgress();
                    if(data){
                        messageAlert("삭제되었습니다",function()  {
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
                    <input type="text"  {...register("GRP_CD")} style={{display:"none"}}  />
                    <Button variant="contained" color="success" onClick={() => {
                                                                            if(childGridRef.current){
                                                                                const tmp = childGridRef.current.addRow();
                                                                                console.log({tmp})
                                                                            }}
                                                                        }>추가</Button>
                    <Button variant="contained" color="success"  onClick={saveHandler}>저장</Button>                                                                        
                    <Button variant="contained" color="success"  onClick={delHandler}>삭제</Button>                                                                        
            </Stack>
            <Grid style={{ height: 200, width: '100%' }} columns={columns}  checkbox={true} showRowStatus={true} ref={childGridRef}  />
            </>
    )
})
  
  

