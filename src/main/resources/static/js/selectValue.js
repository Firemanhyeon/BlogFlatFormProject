$(document).ready(function(){
    $('#orderSelect').val(1);
    function updateBoardList(boards) {
        let container = $('#boardContainer');
        container.empty(); // 기존 내용을 지웁니다.

        boards.forEach(board => {
            let imageHtml = board.firstImagePath
                ? `<img class="card-img-top" src="${board.firstImagePath}" alt="...">`
                : `<img class="card-img-top">`;

            let boardHtml = `
                        <div class="col mb-5">
                            <div class="card h-100">
                                <!-- Product image-->
                                <div>
                                    ${imageHtml}
                                </div>
                                <!-- Product details-->
                                <div class="card-body p-4">
                                    <div class="text-center">
                                        <!-- Product name-->
                                        <h5 class="fw-bolder">${board.boardTitle}</h5>
     
                                    </div>
                                </div>
                                <!-- Product actions-->
                                <div class="card-footer p-4 pt-0 border-top-0 bg-transparent">
                                    <div class="text-center">
                                        <a class="btn btn-outline-dark mt-auto" href="/board/boardInfo/${board.boardId}">자세히 보기</a>
                                    </div>
                                </div>
                            </div>
                        </div>`;
            container.append(boardHtml);
        });
    }

    $('#orderSelect').change(function(){
        let selectVal = $('#orderSelect').val();
        $.ajax({
            url:'/board/selectVal',
            method:'GET',
            data:{
                selectVal:selectVal
            },
            success:function(result){
                console.log(result);
                updateBoardList(result);
            },
            error:function(err){
                console.log(err);
            }
        })
    })

    $('#titleSearchBtn').click(function(){
        let val = $('#searchValue').val();
        if(val===''){
            return;
        }
        $.ajax({
            url:'/board/searchVal',
            method:'GET',
            data:{
                searchVal:val
            },
            success:function(result){
                console.log(result);
                updateBoardList(result);
            },
            error:function(err){
                console.log(err);
            }
        })
    })
})