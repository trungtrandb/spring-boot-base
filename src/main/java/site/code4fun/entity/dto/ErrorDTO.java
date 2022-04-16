package site.code4fun.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ErrorDTO {
	private String type;
	private String title;
	private Integer code;
	private String detail;
	private String instance;

//	"type" (string) - A URI reference [RFC3986] that identifies the
//	problem type.  This specification encourages that, when
//	dereferenced, it provide human-readable documentation for the
//	problem type (e.g., using HTML [W3C.REC-html5-20141028]).  When
//      this member is not present, its value is assumed to be
//      "about:blank".
//
//	o  "title" (string) - A short, human-readable summary of the problem
//	type.  It SHOULD NOT change from occurrence to occurrence of the
//	problem, except for purposes of localization (e.g., using
//			proactive content negotiation; see [RFC7231], Section 3.4).
//
//	o  "status" (number) - The HTTP status code ([RFC7231], Section 6)
//	generated by the origin server for this occurrence of the problem.
//
//	o  "detail" (string) - A human-readable explanation specific to this
//	occurrence of the problem.
//
//	o  "instance" (string) - A URI reference that identifies the specific
//	occurrence of the problem.  It may or may not yield further
//	information if dereferenced.
}