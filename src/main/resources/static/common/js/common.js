// 입력받은 변수의 값 유효성 체크
function isEmpty(value) {
	
  if (value === null || value === undefined) {
    return true;
  }

  if (typeof value === 'string' && value.trim() === '') {
    return true;
  }

  if (Array.isArray(value) && value.length === 0) {
    return true;
  }

  if (typeof value === 'object' && Object.keys(value).length === 0) {
    return true;
  }

  return false;
}

function onlyNumber(input, len = 20) {
    const filteredValue = input.value.replace(/\D/g, '');

    if (filteredValue.length > len) {
        input.value = filteredValue.substring(filteredValue.length - len);
    } else {
        input.value = filteredValue;
    }
}

function inputRegex(input = undefined, type = 'id') {
	
	let regex;
	
    switch (type) {
        case "id":
			regex = /^[a-zA-Z0-9_-]{5,20}$/;
            return regex.test(input);
        case "pwd":
			regex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[!@#$%^&*()\-_=+\\\|\[\]{};:'",.<>\/?]).{8,16}$/;
            return regex.test(input);
        case "email":
			regex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
            return regex.test(input);
        default:
            throw new Error("Invalid type specified");
    }
}

function inpCnvId(input){
	
	if(inputRegex(input.value,'id')) {
		input.setAttribute("aria-invalid", "false");
	}else {
		input.setAttribute("aria-invalid", "true");
	}
	
}