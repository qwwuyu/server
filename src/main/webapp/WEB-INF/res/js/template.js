// 分页模版
template(
    'temp_page',
    '<ul><li class="main-page-list">{{if select != 1}}<a id="{{select - 1}}" href="/{{path}}?page={{select - 1}}" class="page-a">上一页</a>{{/if}}{{each pages}}{{if select != $value}}<a id="{{$value}}" href="/{{path}}?page={{$value}}" class="page-a">{{$value}}</a>{{else if select == $value}}<span class="page-span">{{$value}}</span>{{/if}}{{/each}}{{if select != page}}<a id="{{select + 1}}" href="/{{path}}?page={{select + 1}}" class="page-a">下一页</a>{{/if}}</li><li class="main-page-num"><span>{{count}}</span>条，共<span>{{page}}</span>页</li></ul>{{if send}}<a href="/{{path}}/send" target="_blank" class="page-send">发帖</a>{{/if}}');
// card模版
template(
    'temp_card',
    '<ul>{{each datas}}<li><span class="main-content-title">{{$value.title}}</span><span class="main-content-info">{{$value.nick}}</span><span class="main-content-info">{{$value.time}}</span>{{if $value.userId == userId || auth == 5}}<span id="{{$value.id}}" class="main-content-rm">删除</span>{{/if}}</li>{{/each}}</ul>');
// note模版
template(
    'temp_note',
    '<ul>{{each datas}}<li><a href="/note/content?id={{$value.id}}" target="_blank" class="main-content-click"> <span class="main-content-title">{{$value.title}}</span><span class="main-content-info">{{$value.nick}}</span><span class="main-content-info">{{$value.time}}</span>{{if $value.userId == userId || auth == 5}}<span id="{{$value.id}}" class="main-content-rm">删除</span>{{/if}}</a></li>{{/each}}</ul>');
// flag模版
template(
    'temp_flag',
    '<ul>{{each datas}}<li><span class="main-content-title">{{$value.title}}</span><span class="main-content-info">{{$value.nick}}</span><span class="main-content-info">{{$value.time}}</span></li>{{/each}}</ul>');
// note_content模版
template(
    'temp_note_content',
    '<span class="main-content-title">{{note.title}}</span><span class="main-content-info">{{note.nick}}</span><span class="main-content-info">{{note.time}}</span><span class="main-content-title">{{note.content}}</span>');
