import Dialog from 'art-dialog';

const App = window.App;
export function alert(msg) {
  const d = Dialog({
    title: '提示',
    content: msg,
    okValue: '确定',
    ok() {
      return true;
    },
  });
  d.showModal();
}

export function error(msg) {
  const d = Dialog({
    title: '错误',
    content: msg,
    okValue: '确定',
    ok() {
      return true;
    },
  });
  d.showModal();
}

export function validateField(input) {
  if (input.hasClass('_req') && $.trim(input.val()) === '') {
    error(`${input.attr('label') || `字段[${input.attr('name')}]`}不能为空！`);
    return false;
  }

  return true;
}

export function validateForm(form) {
  const ips = form.find(':text,:password,select');
  const len = ips.length;

  for (let i = 0; i < len; i++) {
    const ip = ips.eq(i);
    if (!validateField(ip)) {
      return false;
    }
  }

  return true;
}

export function resetForm(form) {
  form.reset();
}

export function serializeForm(form) {
  const ips = form.find(':text,:password,select');
  const o = {};

  ips.each(() => {
    const $this = $(this),
      k = $this.attr('name'),
      v = $.trim($this.val());

    o[k] = v;
  });

  return o;
}

export function submitForm(form, url) {
  const u = url || form.attr('action');
  const m = form.attr('method') || 'post';

  return request(u, {
    method: m,
    data: serializeForm(form),
    successFn(r) {
      return r;
    },
  });
}

export function request(url, config) {
  config = config || {};
  const method = config.method || 'get';

  return $.ajax({
    url: App.basePath + url,
    type: method,
    dataType: 'json',
    data: config.params,
    success: config.successFn,
    error: config.errorFn,
  });
}
