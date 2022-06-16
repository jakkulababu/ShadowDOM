/**
This is the injected function to the browser to search ShadowRoot
its a naive recursively search in the DOM, However its performance on moder browsers is impressive.
Usually is a lot faster than the endpoints of the MDM
**/
window.searchAZShadow= (root, selector)=>{

  const valid=res=>{
    return res!==undefined && res.length>0;
  };
  const filterVisible=res=> {
    res=Array.prototype.slice.call(res || []);
    return res===null ? null : res.filter(x=> x.offsetParent!==null || res.type!=="file") ;
  };

  const search = (root,selector)=>{
    let res= root.querySelectorAll(selector);
    res=filterVisible(res);
	  if ( valid(res))
			return res;
	  for( let ele of  root.querySelectorAll("*:not(script):not([hidden])")){
			if(ele.shadowRoot){
			  res= searchAZShadow(ele.shadowRoot, selector);
				res=filterVisible(res);
			  if ( valid(res))
					return res;
			}
  }};
  return search(root,selector);
};
/**
Short hand to test selectors on the browser
**/
window.debugAZShadow=(selector)=>{
    return searchAZShadow(document.body, selector);
};

window.azElementByXpath=(selector,root)=>{
  return document.evaluate(selector, root, null, XPathResult.FIRST_ORDERED_NODE_TYPE, null).singleNodeValue;
};