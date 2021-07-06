<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2021/6/22
  Time: 14:16
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<script type="text/javascript" src="/day03/js/jquery-3.0.0.js"></script>
<html>
  <head>
    <title>鑫哥的官方网站</title>
    <style>
      body{margin: 0}
      .div{
        position: absolute;
        top:235px;
        left: 445px;
        border: 2px solid blue;
        border-top-color: white;
        border-radius: 0 0 10px 10px;
        width: 559px;
        padding-left: 10px;
      }
      #province{
        position:absolute;
        top:191px;
        left: 445px;
        width:573px;
        height: 45px;
        padding-left: 10px;
        font-size: 16px;
        border-radius: 10px 0 0 10px;
        outline:none;
        border:2px solid gray;
      }
      #province:focus{
        border: 2px solid blue;
        border-radius: 10px 0 0 0;
        border-bottom-color: lightgray;
      }
      #div4{
        background-color: blue;
        color: white;
        position: absolute;
        top: 191px;
        left: 1018px;
        height: 45px;
        width: 108px;
        text-align: center;
        font-size: 17px;
        line-height: 40px;
        border-radius: 0 10px 10px 0;
      }
      #img1{
        position: absolute;
        top: 196px;
        left: 968px;
        width: 33px;
        height: 35px;
      }
      #img2{
        position: absolute;
        top: 100px;
        left: 688px;
        width: 170px;
      }
    </style>
  </head>
  <body>
  <img src="/day03/img/2.jpg" alt="出错啦" id="img2">
  <input name="province" id="province" type="text"/><img src="/day03/img/1.jpg" alt="出错了" id="img1">
  <div id="div4">百度一下</div>
  <div id="parent">
  <div class="select" id = "select"></div>
  </div>
  </body>
  <script>
    var value= "";
    $("input[name='province']").focus(
            function() {
              $.post("/day03/provinceSelect", {}, function (data) {
                $.each(data, function (index, element) {
                  var div = "<div class=e id=" + element.id + " value=" + element.name + ">" + element.name + "</div>";
                  $("div[id='select']").append(div);
                  $("#"+element.id).height("30px");
                  $("#"+element.id).mouseover(
                          function h() {
                            value = element.name;
                          }
                  );
                });
                $("div[id='select']").addClass("div")
              }, "json");
            }
    );

      $("input[name='province']").blur(
              function () {
                $("input[name='province']").val(value);
                var div1 = "<div class=select id=select></div>";
                $("div[id='parent']").html(div1);
              }
      );


  </script>
</html>
