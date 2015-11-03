for ((i=0; i<8; i++)); do
    mkdir peer$i
    for ((j=0; j<10; j++)); do
        base64 /dev/urandom | head -c $((RANDOM%20000+1000)) > peer$i/file-p$i-0$j
    done
done
