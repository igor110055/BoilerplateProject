import Grid,{GridHandler} from '@/form-components/Grid';
import GridCombo from '@/form-components/GridCombo';
import GridTextEditor from '@/form-components/GridTextEditor';
import { useForm } from "react-hook-form";
import {FormSelect}  from '@/form-components/FormSelect';
import {FormTextFiled} from '@/form-components/FormTextFiled';
import send from '@/utils/send';
import {Stack,Button} from '@mui/material'
import {MenuContext} from "@/store/MenuStore";
import { CM_4200__역할관리_role_cd } from '@/mdi-pages/CM/CM_4200__역할관리_role_cd';
import { CM_4200__역할관리_user_role_cd, CM_4200__역할관리_user_role_cd_sub2Handler } from '@/mdi-pages/CM/CM_4200__역할관리_user_role_cd';
import { CM_4200__역할관리_tree } from '@/mdi-pages/CM/CM_4200__역할관리_tree';
import React, { createContext,useState,useContext,useRef} from "react";
import { CalculatedColumn } from 'react-data-grid';

export  const CM_4200__역할관리 = () => {   
    const childRef = useRef<CM_4200__역할관리_user_role_cd_sub2Handler>(null);
    const fnSearch= async (roleCd: string) => {
        if(childRef.current){
            childRef.current.fnSearchGrid(roleCd);

        }
    }
    const gridOnRowClickHandler  = (row: any, column: CalculatedColumn<any, any>)=>{
        //console.log(row.GRP_CD);       
        fnSearch(row.ROLE_CD);
    }

    return (
            <>
              <CM_4200__역할관리_role_cd  onRowClick={gridOnRowClickHandler}   />
              <CM_4200__역할관리_user_role_cd  ref={childRef}  />
              <CM_4200__역할관리_tree   />
            </>
    )
  }
  
  

