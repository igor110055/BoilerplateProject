import {forwardRef,useState,useEffect,useImperativeHandle,useRef} from 'react';
import Button from '@mui/material/Button';
import Dialog from '@mui/material/Dialog';
import DialogActions from '@mui/material/DialogActions';
import DialogContent from '@mui/material/DialogContent';
import DialogContentText from '@mui/material/DialogContentText';
import DialogTitle from '@mui/material/DialogTitle';
import IconButton from '@mui/material/IconButton';
import CloseIcon from '@mui/icons-material/Close';
import Draggable from 'react-draggable';
import Paper, { PaperProps } from '@mui/material/Paper';
import { Breakpoint } from '@mui/material';
  

export type InlineDialogHandler = {
    setValue:(value:JSX.Element, fn?:any,fullWidth?:boolean,maxWidth?:Breakpoint)=>void;
    close:()=>void;
};

function PaperComponent(props: PaperProps) {
    return (
      <Draggable
        handle="#alert-dialog-title"
        cancel={'[class*="MuiDialogContent-root"]'}
      >
        <Paper {...props} />
      </Draggable>
    );
  }

const InlineDialog = forwardRef<InlineDialogHandler,any>((props,ref)=> {
  const [open, setOpen] = useState(false);
  const [value, setValue] = useState<JSX.Element>();
  const [fullWidth, setFullWidth] = useState(false);
  const [maxWidth, setMaxWidth] = useState<Breakpoint>("sm");

  
  const localFn = useRef<Function>();

  
  useImperativeHandle(ref, () => ({
    setValue(value:JSX.Element, fn:any,fullWidth?:boolean,maxWidth?:Breakpoint) {
        console.log("AlertDialog--1",fullWidth,maxWidth);
        setOpen(true);
        setValue(value);
        localFn.current=fn;
        if(maxWidth){
            console.log(maxWidth);
            setMaxWidth(maxWidth)
        }
        if(fullWidth){
            setFullWidth(fullWidth)
        }
        
    },close(){
        console.log("cccccccccc")
        handleClose();
    }
  }))

  const handleClose = () => {
    setOpen(false);
    if(localFn.current){
        //console.log("aaaaaaaaaa")
        localFn.current();
    }
  };

  return (
      <Dialog
        aria-labelledby="alert-dialog-title"
        aria-describedby="alert-dialog-description"
        PaperComponent={PaperComponent}
        scroll={"paper"}
        fullWidth={fullWidth}
        maxWidth={maxWidth}
        open={open}
      >
        <DialogTitle id="alert-dialog-title" style={{ cursor: 'move' }}>
          InlineDialog
          <IconButton
          aria-label="close"
          onClick={handleClose}
          sx={{
            position: 'absolute',
            right: 8,
            top: 8,
            color: (theme) => theme.palette.grey[500],
          }}
        >
          <CloseIcon />
        </IconButton>
        </DialogTitle>
        <DialogContent>
            {value}
          <DialogContentText id="alert-dialog-description">
            
          </DialogContentText>
        </DialogContent>
        <DialogActions>
          <Button onClick={handleClose} autoFocus>
            Close
          </Button>
        </DialogActions>
      </Dialog>
  );
});


export default InlineDialog;