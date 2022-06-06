class imageRenderer {
  constructor(props) {
		const el = document.createElement('img');
		el.src = 'some-image-link';
		el.width =30;
		el.height =30;
		this.el = el;
		const viewer = new Viewer(this.el, {
			  toolbar: {
			    zoomIn: 4,
			    zoomOut: 4,
			    oneToOne: 4,
			    reset: 4,
			    prev: 4,
			    play: {
			      show: 4,
			      size: 'large',
			    },
			    next: 4,
			    rotateLeft: 4,
			    rotateRight: 4,
			    flipHorizontal: 4,
			    flipVertical: 4,
			  },
  		});
		this.render(props);
		
	}

    getElement() {
      return this.el;
    }

    render(props) {
    	//props.value는 파일 id
    	//props.rowKey  ==> 16
		var row_data=props.grid.getRow(props.rowKey);
		var file_id = row_data["FILE_ID"]
		var ext = row_data["EXT"]
		if(ext=='png') 	{
			var file_src= '/file_dwn/'+	file_id;
	    	this.el.src = file_src;
		} else {
			this.el.style.display="none";
		}
	    
  	}
  }