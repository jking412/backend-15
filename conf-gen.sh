template_upstream="upstream desktop_%s {
   server uos-svc-%s:80;
}
"
template_server="server {
  listen 80;
  %s
}
"
template_location="location /env/%s {
  proxy_pass http://desktop_%s/;
  proxy_set_header Host \$http_host;
  proxy_set_header Accept-Encoding gzip;
}
 "
template_location_websockify="location /env/%s/websockify {
  proxy_pass http://desktop_%s/;
  proxy_http_version 1.1;
  proxy_set_header Upgrade \$http_upgrade;
  proxy_set_header Connection \"Upgrade\";
  proxy_set_header Host \$host;
}
"


output_result=""

output_upstream=""
for arg in $@
do
    output_upstream=$output_upstream$(printf "$template_upstream" $arg $arg)
done

output_location=""
output_location_websockify=""
for arg in $@
do
    output_location=$output_location$(printf "$template_location" $arg $arg)
    output_location_websockify=$output_location_websockify$(printf "$template_location_websockify" $arg $arg)
done

output_server=$(printf "$template_server" "$output_location$output_location_websockify")

output_result=$output_result$output_upstream$output_server

echo "$output_result" > /etc/nginx/conf.d/default.conf
#echo $output_result

