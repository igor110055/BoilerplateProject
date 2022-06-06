import Grid,{GridHandler} from '@/form-components/Grid';
import GridCombo from '@/form-components/GridCombo';
import GridTextEditor from '@/form-components/GridTextEditor';
import { useForm } from "react-hook-form";
import {FormSelect}  from '@/form-components/FormSelect';
import {FormTextFiled} from '@/form-components/FormTextFiled';
import send from '@/utils/send';
import {Stack,Button} from '@mui/material'
import {MenuContext} from "@/store/MenuStore";
import { CM_6200__게시판_list } from '@/mdi-pages/CM/CM_6200__게시판_list';
import { CM_6200__게시판_view } from '@/mdi-pages/CM/CM_6200__게시판_view';
import React, { createContext,useState,useContext,useRef} from "react";
import { CalculatedColumn } from 'react-data-grid';
import { CM_6200__게시판_edit } from './CM_6200__게시판_edit';
import { CM_6200__게시판_new } from './CM_6200__게시판_new';

export interface IPageProps {
    setMode: React.Dispatch<React.SetStateAction<string>>;
    setBrdSeq?: React.Dispatch<React.SetStateAction<string>>;
    brdSeq?:string;
    grpSeq?:string;
}
    

export  const CM_6200__게시판 = () => {   
    const [mode,setMode] = useState<string>("list"); /*list, view , edit ,new */
    const [brdSeq,setBrdSeq] = useState<string>("");
    const [grpSeq,setGrpSeq] = useState<string>("");

    return (
            <>
            {mode=="list" &&
                <CM_6200__게시판_list  setMode={setMode}   setBrdSeq={setBrdSeq}  />
            }
            {mode=="view" &&
                <CM_6200__게시판_view  setMode={setMode}  setBrdSeq={setBrdSeq} brdSeq={brdSeq}  />
            }

            {(mode=="new") &&
                <CM_6200__게시판_new  setMode={setMode}  setBrdSeq={setBrdSeq} grpSeq={grpSeq}  />
            }   
            {( mode=="edit" ) &&
                <CM_6200__게시판_edit  setMode={setMode} setBrdSeq={setBrdSeq} brdSeq={brdSeq}   />
            }   
            </>
    )
  }
  
  

