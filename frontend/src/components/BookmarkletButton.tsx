import React from 'react';

function BookmarkletButton() {
  const {protocol, host} = window.location;
  const bookmarkletUrl = encodeURI(`javascript:(function(){var w=window,d=document,e=encodeURIComponent;var c=function(s, wordCount){return (s||'').trim().replace(/\\s+/g,' ').split(' ').filter(w=>w.length>0).slice(0,wordCount).join(' ')};var m=(function(){var _m={},me =d.getElementsByTagName('meta');for(var i=0;i<me.length;i++){me[i].getAttributeNames().forEach(a=>{var ct=me[i]['content'];if(ct){if(a==='name'){_m[me[i][a]]=ct;}else if(a==='property'){_m[me[i].getAttribute(a)]=ct;}}});}return _m;})();var t=(d.title||m['title']||m['og:title']||m['twitter:title']||'');var p=c((m['description']||m['og:description']||m['twitter:description']||(d.body.innerText||'')),30);var o=w.open('${protocol}//${host}/add?url='+e(d.location.href)+'&title='+e(t)+'&description='+e(p), '_tlrl','left='+((w.screenX||w.screenLeft)+10)+',top='+((w.screenY||w.screenTop)+10)+',height=310,width=600,resizable=1,alwaysRaised=1,status=0');w.setTimeout(function(){o.focus();},500);})();`);
  return <span dangerouslySetInnerHTML={{__html: `<a href=${bookmarkletUrl} title="Save to TLRL" class="f6 link br1 ph2 pv0 dib btn white bg-blue">Save to TLRL</a>`}}></span>;
}

export default BookmarkletButton;