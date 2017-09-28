
namespace java Jiuyan.Thrift.Header
struct Header{
    1:string trace_id,
    2:string span_id,
    3:string parent_span_id
}
service RequestHeader {
    string   _header(1:Header header)
}