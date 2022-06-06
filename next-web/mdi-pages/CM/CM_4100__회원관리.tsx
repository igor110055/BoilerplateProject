import Grid,{GridHandler} from '@/form-components/Grid';
import GridTextEditor from '@/form-components/GridTextEditor';
import React, { useState,useEffect,useRef,useReducer,useContext } from "react";
import send from '@/utils/send';
import {Stack,Button} from '@mui/material'
import {MenuContext} from "@/store/MenuStore";
import { CalculatedColumn } from 'react-data-grid';
import { CM_4100__회원관리_modify } from './CM_4100__회원관리_modify';
import GridCombo from '@/form-components/GridCombo';



export  const CM_4100__회원관리 = () => {   
    const menuStoreContext = useContext(MenuContext)
    const {messageAlert,messageConfirm } = menuStoreContext;
    const [mode,setMode] = useState<string>("list"); /*list, view , edit ,new */
    const [userUid,setUserUid] = useState(0);

    const childRef = useRef<GridHandler>(null);
    const [useYnData,setUseYnData] = useState([]);
    const fnSearch= async () => {
        if(childRef.current){
            childRef.current.loadData({
                                        BR:"BR_CM_USER_FIND",
                                        PARAM: {
                                            brRq : 'IN_DATA'
                                            ,brRs : 'OUT_DATA'
                                            ,IN_DATA : [ {} ]
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
        fnSearch()
    },[]);

    const columns = [
        { key: 'USER_UID', name: '사용자UID', width: 60 ,  sortable: true},
        { key: 'USER_NM', name: '사용자명', width: 100 ,editor: GridTextEditor},
        { key: 'NICK_NM', name: '닉네임', width: 100 ,editor: GridTextEditor},
        { key: "USER_ID", name: '사용자ID', width: 200 ,  sortable: true  ,editor: GridTextEditor},
        { key: 'GNDR', name: '성별', width: 60 ,editor: GridTextEditor},            
        { key: 'BIRTH', name: 'BIRTH', width: 60 ,editor: GridTextEditor},            
        { key: 'PWD', name: '비밀번호', width: 200 ,editor: GridTextEditor},         
        { key: 'EMAIL', name: '이메일', width: 200 ,editor: GridTextEditor},         
        { key: 'USE_YN', name: '사용여부', width: 60  ,editor: (p:any) => (<GridCombo p={p} options={useYnData}  />) , editorOptions: {editOnClick: true}},
        { key: 'LST_ACC_DTM', name: '마지막접속일', width: 200 ,editor: GridTextEditor},         
        { key: 'CRT_DTM', name: '생성일', width: 160 },
        { key: 'UPDT_DTM', name: '수정일', width: 160 },
    ];

    const delHandler=(event: React.MouseEvent<HTMLButtonElement>) => {
        event.preventDefault();
        if(childRef.current){
            const data = childRef.current.getCheckedData();
            console.log({data});
            if(data.length<=0) {
                messageAlert('선택된 항목이 없습니다.');
                return;
            }
            messageConfirm("삭제하시겠습니까?",function()  {
                //_this.showProgress();	
                send('BR_CM_USER_rm',{
                    brRq 		: 'IN_DATA,SESSION'
                    ,brRs 		: ''
                    ,IN_DATA	: data
                }).then(function(data){
                    //_this.hideProgress();
                    if(data){
                        messageAlert("삭제되었습니다",function()  {
                            fnSearch()
                        });
                    }
                })                      
            })
        }
    }

        
    const gridOnRowClickHandler  = (row: any, column: CalculatedColumn<any, any>)=>{
        setMode("edit")
        setUserUid(row.USER_UID)
    }

    const newHandler=()=>{
        setMode("new");
        setUserUid(0);
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
                    <Button variant="contained" color="success"   onClick={fnSearch}>조회</Button>
                    <Button variant="contained" color="success"  onClick={delHandler}>삭제</Button>                                                                        
                    <Button variant="contained" color="success" onClick={newHandler}>신규</Button>        
            </Stack>
            <Grid style={{ height: 200, width: '100%' }} columns={columns}  checkbox={true} showRowStatus={true} ref={childRef} onRowClick={gridOnRowClickHandler}  />
            <CM_4100__회원관리_modify  userUid={userUid} fnSearch = {fnSearch}/>
            </>
    )
  }
  
  

