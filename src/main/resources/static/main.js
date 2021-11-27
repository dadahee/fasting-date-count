const main = {
    init : function () {
        var _this = this;

        let saveFoodButtons = document.getElementsByClassName("btn-food-save");
        for (let i=0; i<saveFoodButtons.length; i++) {
            saveFoodButtons[i].addEventListener('click', this.saveFood);
        }

        let updateFoodButtons = document.getElementsByClassName("btn-food-update");
        for (let i=0; i<updateFoodButtons.length; i++) {
            updateFoodButtons[i].addEventListener('click', this.updateFood);
        }

        let deleteFoodButtons = document.getElementsByClassName("btn-food-delete");
        for (let i=0; i<deleteFoodButtons.length; i++) {
            deleteFoodButtons[i].addEventListener('click', this.deleteFood);
        }

        let saveReviewButtons = document.getElementsByClassName("btn-review-save");
        for (let i=0; i<saveReviewButtons.length; i++) {
            saveReviewButtons[i].addEventListener('click', this.saveReview);
        }

        let updateReviewButtons = document.getElementsByClassName("btn-review-update");
        for (let i=0; i<updateReviewButtons.length; i++) {
            updateReviewButtons[i].addEventListener('click', this.updateReview);
        }

        let deleteReviewButtons = document.getElementsByClassName("btn-review-delete");
        for (let i=0; i<deleteReviewButtons.length; i++) {
            deleteReviewButtons[i].addEventListener('click', this.deleteReview);
        }
    },

    saveFood : function () {
        const data = {
            name: $('#name').val(),
            startDate: $('#start-date').val(),
        };

        $.ajax({
            type: 'POST',
            url: '/api/food',
            dataType: 'json',
            contentType:'application/json; charset=utf-8',
            data: JSON.stringify(data)
        }).done(function() {
            alert('음식이 등록되었습니다.');
            window.location.href = '/food';
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });
    },
    updateFood : function () {
        const data = {
            id: Number($('.food-id').attr('id')),
            name: $('#name').val(),
            startDate: $('#start-date').val(),
        };

        $.ajax({
            type: 'PUT',
            url: '/api/food/' + data.id,
            dataType: 'json',
            contentType:'application/json; charset=utf-8',
            data: JSON.stringify(data)
        }).done(function() {
            alert('음식이 수정되었습니다.');
            window.location.href = '/food';
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });
    },
    deleteFood : function () {
        const id =  Number($(this).attr('id'));

        $.ajax({
            type: 'DELETE',
            url: '/api/food/' + id,
            dataType: 'json',
            contentType:'application/json; charset=utf-8'
        }).done(function() {
            alert('음식이 삭제되었습니다.');
            window.location.href = '/food';
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });
    },

    saveReview : function () {
        const data = {
            foodId: Number($('.food-id').attr('id')),
            date: $('#date').val(),
            title: $('#title').val(),
            content: $('#content').val(),
            fasted: $("#fasted").is(":checked")? "true" : "false"
        };

        $.ajax({
            type: 'POST',
            url: '/api/food/' + data.foodId + '/reviews',
            dataType: 'json',
            contentType:'application/json; charset=utf-8',
            data: JSON.stringify(data)
        }).done(function() {
            alert('리뷰가 추가되었습니다.');
            window.location.href = '/food/' + data.foodId + '/reviews';
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });
    },
    updateReview : function () {
        const data = {
            id: Number($(".review-id").attr('id')),
            foodId: Number($('.food-id').attr('id')), //될까
            date: $('#date').val(),
            title: $('#title').val(),
            content: $('#content').val(),
            fasted: $("#fasted").is(":checked")? "true" : "false"
        };

        $.ajax({
            type: 'PUT',
            url: '/api/food/' + data.foodId + '/reviews/' + data.id,
            dataType: 'json',
            contentType:'application/json; charset=utf-8',
            data: JSON.stringify(data)
        }).done(function() {
            alert('리뷰가 수정되었습니다.');
            window.location.href = '/food/' + data.foodId + '/reviews';
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });
    },
    deleteReview : function () {
        const foodId = Number($(".food").attr("id"));
        const reviewId = Number($(".btn-review-delete").attr("id"));

        $.ajax({
            type: 'DELETE',
            url: '/api/food/' + foodId + '/reviews/' + reviewId,
            dataType: 'json',
            contentType:'application/json; charset=utf-8'
        }).done(function() {
            alert('리뷰가 삭제되었습니다.');
            window.location.href = '/food/' + foodId + '/reviews';
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });
    }
};

main.init();