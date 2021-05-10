package com.kenzahn.zahn.rest;
import com.kenzahn.zahn.model.CardContentRes2;

import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient
{
    public static String data="{\n" +
            "  \"status\": \"Success\",\n" +
            "  \"data\": [\n" +
            "    {\n" +
            "      \"ExamID\": 4956,\n" +
            "      \"ExamTypeID\": 24,\n" +
            "      \"Randomized\": false,\n" +
            "      \"Timed\": false,\n" +
            "      \"ExamType\": \"Q&A\",\n" +
            "      \"QuestionCount\": 12,\n" +
            "      \"SortOrder\": 0,\n" +
            "      \"ExamName\": \"Income Tax\",\n" +
            "      \"Version\": 20.3,\n" +
            "      \"Active\": true,\n" +
            "      \"ExamModuleID\": 0,\n" +
            "      \"ExamQuestions\": [\n" +
            "        {\n" +
            "          \"ExamQuestionID\": 189607,\n" +
            "          \"CreateDate\": \"2020-06-01T14:36:00\",\n" +
            "          \"AnswerA\": \"Congressional Committee Reports\",\n" +
            "          \"AnswerB\": \"Supreme Court rulings\",\n" +
            "          \"AnswerC\": \"Treasury regulations\",\n" +
            "          \"AnswerD\": \"Internal Revenue Code\",\n" +
            "          \"AnswerE\": \"\",\n" +
            "          \"AnswerF\": \"\",\n" +
            "          \"AnswerI\": \"\",\n" +
            "          \"AnswerII\": \"\",\n" +
            "          \"AnswerIII\": \"\",\n" +
            "          \"AnswerIV\": \"\",\n" +
            "          \"AnswerV\": \"\",\n" +
            "          \"AnswerVI\": \"\",\n" +
            "          \"ExamID\": 4956,\n" +
            "          \"CorrectAnswer\": \"D\",\n" +
            "          \"Explanation\": \"Federal tax law means the Internal Revenue Code.\",\n" +
            "          \"Question\": \"Which is the primary sourse of all tax law?\",\n" +
            "          \"ExamCaseID\": 0,\n" +
            "          \"SortOrder\": 0,\n" +
            "          \"ExamSectionID\": 0,\n" +
            "          \"PageBreak\": false,\n" +
            "          \"PageBreak2\": false,\n" +
            "          \"ExamSection\": null\n" +
            "        },\n" +
            "        {\n" +
            "          \"ExamQuestionID\": 189608,\n" +
            "          \"CreateDate\": \"2020-06-01T15:30:00\",\n" +
            "          \"AnswerA\": \"Step transaction\",\n" +
            "          \"AnswerB\": \"Sham transaction\",\n" +
            "          \"AnswerC\": \"Assignment of income\",\n" +
            "          \"AnswerD\": \"Constructive receipt\",\n" +
            "          \"AnswerE\": \"\",\n" +
            "          \"AnswerF\": \"\",\n" +
            "          \"AnswerI\": \"\",\n" +
            "          \"AnswerII\": \"\",\n" +
            "          \"AnswerIII\": \"\",\n" +
            "          \"AnswerIV\": \"\",\n" +
            "          \"AnswerV\": \"\",\n" +
            "          \"AnswerVI\": \"\",\n" +
            "          \"ExamID\": 4956,\n" +
            "          \"CorrectAnswer\": \"C\",\n" +
            "          \"Explanation\": \"Income <u>paid</u>&nbsp;is key to the answer\",\n" +
            "          \"Question\": \"Which tax law doctrine affects this example?&nbsp; Larry owns L, Inc., an S Corporation.&nbsp; He directs all his income to be paid to his daughter.&nbsp; He reports no income.\",\n" +
            "          \"ExamCaseID\": 0,\n" +
            "          \"SortOrder\": 1,\n" +
            "          \"ExamSectionID\": 0,\n" +
            "          \"PageBreak\": false,\n" +
            "          \"PageBreak2\": false,\n" +
            "          \"ExamSection\": null\n" +
            "        },\n" +
            "        {\n" +
            "          \"ExamQuestionID\": 189609,\n" +
            "          \"CreateDate\": \"2020-06-01T15:33:00\",\n" +
            "          \"AnswerA\": \"Congressional Committee hearings\",\n" +
            "          \"AnswerB\": \"Commerce Clearing House\",\n" +
            "          \"AnswerC\": \"Charitable Children's home\",\n" +
            "          \"AnswerD\": \"\",\n" +
            "          \"AnswerE\": \"\",\n" +
            "          \"AnswerF\": \"\",\n" +
            "          \"AnswerI\": \"\",\n" +
            "          \"AnswerII\": \"\",\n" +
            "          \"AnswerIII\": \"\",\n" +
            "          \"AnswerIV\": \"\",\n" +
            "          \"AnswerV\": \"\",\n" +
            "          \"AnswerVI\": \"\",\n" +
            "          \"ExamID\": 4956,\n" +
            "          \"CorrectAnswer\": \"B\",\n" +
            "          \"Explanation\": \"CCH is a tax reference source, not tax law\",\n" +
            "          \"Question\": \"What does CCH stand for?\",\n" +
            "          \"ExamCaseID\": 0,\n" +
            "          \"SortOrder\": 2,\n" +
            "          \"ExamSectionID\": 0,\n" +
            "          \"PageBreak\": false,\n" +
            "          \"PageBreak2\": false,\n" +
            "          \"ExamSection\": null\n" +
            "        },\n" +
            "        {\n" +
            "          \"ExamQuestionID\": 189610,\n" +
            "          \"CreateDate\": \"2020-06-01T15:36:00\",\n" +
            "          \"AnswerA\": \"Yourself\",\n" +
            "          \"AnswerB\": \"His CPA\",\n" +
            "          \"AnswerC\": \"His corporate attorney\",\n" +
            "          \"AnswerD\": \"Another CFP® practitioner\",\n" +
            "          \"AnswerE\": \"\",\n" +
            "          \"AnswerF\": \"\",\n" +
            "          \"AnswerI\": \"\",\n" +
            "          \"AnswerII\": \"\",\n" +
            "          \"AnswerIII\": \"\",\n" +
            "          \"AnswerIV\": \"\",\n" +
            "          \"AnswerV\": \"\",\n" +
            "          \"AnswerVI\": \"\",\n" +
            "          \"ExamID\": 4956,\n" +
            "          \"CorrectAnswer\": \"B\",\n" +
            "          \"Explanation\": \"Nothing indicates that you or another CFP<sup>&#174;</sup>practicioner can represent him before the IRS.&nbsp; His problem is personal not corporate.&nbsp;&nbsp;\",\n" +
            "          \"Question\": \"Tom needs to go to the IRS due to a tax dispute.&nbsp; Who would you recommend?\",\n" +
            "          \"ExamCaseID\": 0,\n" +
            "          \"SortOrder\": 3,\n" +
            "          \"ExamSectionID\": 0,\n" +
            "          \"PageBreak\": false,\n" +
            "          \"PageBreak2\": false,\n" +
            "          \"ExamSection\": null\n" +
            "        },\n" +
            "        {\n" +
            "          \"ExamQuestionID\": 189611,\n" +
            "          \"CreateDate\": \"2020-06-01T15:39:00\",\n" +
            "          \"AnswerA\": \"Yes\",\n" +
            "          \"AnswerB\": \"No\",\n" +
            "          \"AnswerC\": \"\",\n" +
            "          \"AnswerD\": \"\",\n" +
            "          \"AnswerE\": \"\",\n" +
            "          \"AnswerF\": \"\",\n" +
            "          \"AnswerI\": \"\",\n" +
            "          \"AnswerII\": \"\",\n" +
            "          \"AnswerIII\": \"\",\n" +
            "          \"AnswerIV\": \"\",\n" +
            "          \"AnswerV\": \"\",\n" +
            "          \"AnswerVI\": \"\",\n" +
            "          \"ExamID\": 4956,\n" +
            "          \"CorrectAnswer\": \"A\",\n" +
            "          \"Explanation\": \"Qualified dividends and long-term gains are part of total income.&nbsp; Yes, they are taxed differently but they are included in total.\",\n" +
            "          \"Question\": \"Are qualified dividends and long-term gains part of the 1040 form for total income?\",\n" +
            "          \"ExamCaseID\": 0,\n" +
            "          \"SortOrder\": 4,\n" +
            "          \"ExamSectionID\": 0,\n" +
            "          \"PageBreak\": false,\n" +
            "          \"PageBreak2\": false,\n" +
            "          \"ExamSection\": null\n" +
            "        },\n" +
            "        {\n" +
            "          \"ExamQuestionID\": 189612,\n" +
            "          \"CreateDate\": \"2020-06-01T15:41:00\",\n" +
            "          \"AnswerA\": \"Yes\",\n" +
            "          \"AnswerB\": \"No\",\n" +
            "          \"AnswerC\": \"\",\n" +
            "          \"AnswerD\": \"\",\n" +
            "          \"AnswerE\": \"\",\n" +
            "          \"AnswerF\": \"\",\n" +
            "          \"AnswerI\": \"\",\n" +
            "          \"AnswerII\": \"\",\n" +
            "          \"AnswerIII\": \"\",\n" +
            "          \"AnswerIV\": \"\",\n" +
            "          \"AnswerV\": \"\",\n" +
            "          \"AnswerVI\": \"\",\n" +
            "          \"ExamID\": 4956,\n" +
            "          \"CorrectAnswer\": \"A\",\n" +
            "          \"Explanation\": \"Alimony for divorce prior to 2019 is deductible.&nbsp; The year is important.&nbsp; Today it is not deductible nor income to the spouse.&nbsp;&nbsp;\",\n" +
            "          \"Question\": \"Terry divorced Susan in 2018.&nbsp; He has to pay alimony.&nbsp; Is the alimony deductible on his 1040 form?\",\n" +
            "          \"ExamCaseID\": 0,\n" +
            "          \"SortOrder\": 5,\n" +
            "          \"ExamSectionID\": 0,\n" +
            "          \"PageBreak\": false,\n" +
            "          \"PageBreak2\": false,\n" +
            "          \"ExamSection\": null\n" +
            "        },\n" +
            "        {\n" +
            "          \"ExamQuestionID\": 189613,\n" +
            "          \"CreateDate\": \"2020-06-01T15:43:00\",\n" +
            "          \"AnswerA\": \"Yes\",\n" +
            "          \"AnswerB\": \"No, it is tax free\",\n" +
            "          \"AnswerC\": \"Yes, if taxable income exceeds a threshold\",\n" +
            "          \"AnswerD\": \"\",\n" +
            "          \"AnswerE\": \"\",\n" +
            "          \"AnswerF\": \"\",\n" +
            "          \"AnswerI\": \"\",\n" +
            "          \"AnswerII\": \"\",\n" +
            "          \"AnswerIII\": \"\",\n" +
            "          \"AnswerIV\": \"\",\n" +
            "          \"AnswerV\": \"\",\n" +
            "          \"AnswerVI\": \"\",\n" +
            "          \"ExamID\": 4956,\n" +
            "          \"CorrectAnswer\": \"A\",\n" +
            "          \"Explanation\": \"It is a part of MAGI.&nbsp; It affects the percentage of Social Security benefits that are taxable.&nbsp; It is tax free!\",\n" +
            "          \"Question\": \"Is municiple income reported on the 1040 form?\",\n" +
            "          \"ExamCaseID\": 0,\n" +
            "          \"SortOrder\": 6,\n" +
            "          \"ExamSectionID\": 0,\n" +
            "          \"PageBreak\": false,\n" +
            "          \"PageBreak2\": false,\n" +
            "          \"ExamSection\": null\n" +
            "        },\n" +
            "        {\n" +
            "          \"ExamQuestionID\": 189614,\n" +
            "          \"CreateDate\": \"2020-06-01T15:45:00\",\n" +
            "          \"AnswerA\": \"$0\",\n" +
            "          \"AnswerB\": \"$243,000\",\n" +
            "          \"AnswerC\": \"$258,000\",\n" +
            "          \"AnswerD\": \"Not enough information\",\n" +
            "          \"AnswerE\": \"\",\n" +
            "          \"AnswerF\": \"\",\n" +
            "          \"AnswerI\": \"\",\n" +
            "          \"AnswerII\": \"\",\n" +
            "          \"AnswerIII\": \"\",\n" +
            "          \"AnswerIV\": \"\",\n" +
            "          \"AnswerV\": \"\",\n" +
            "          \"AnswerVI\": \"\",\n" +
            "          \"ExamID\": 4956,\n" +
            "          \"CorrectAnswer\": \"C\",\n" +
            "          \"Explanation\": \"Unless is says a portion is taxable interest dividends or gains, nothing is income taxable.\",\n" +
            "          \"Question\": \"Cliff inherited $258,000 after his mother's estate as settled.&nbsp; How much does he have to report as taxable income?\",\n" +
            "          \"ExamCaseID\": 0,\n" +
            "          \"SortOrder\": 7,\n" +
            "          \"ExamSectionID\": 0,\n" +
            "          \"PageBreak\": false,\n" +
            "          \"PageBreak2\": false,\n" +
            "          \"ExamSection\": null\n" +
            "        },\n" +
            "        {\n" +
            "          \"ExamQuestionID\": 189615,\n" +
            "          \"CreateDate\": \"2020-06-01T15:48:00\",\n" +
            "          \"AnswerA\": \"Company car for business purposes\",\n" +
            "          \"AnswerB\": \"Health insurance paid for self-employed owners\",\n" +
            "          \"AnswerC\": \"Premium for group life insurance up to $50,000\",\n" +
            "          \"AnswerD\": \"Occasional overtime meal money\",\n" +
            "          \"AnswerE\": \"\",\n" +
            "          \"AnswerF\": \"\",\n" +
            "          \"AnswerI\": \"\",\n" +
            "          \"AnswerII\": \"\",\n" +
            "          \"AnswerIII\": \"\",\n" +
            "          \"AnswerIV\": \"\",\n" +
            "          \"AnswerV\": \"\",\n" +
            "          \"AnswerVI\": \"\",\n" +
            "          \"ExamID\": 4956,\n" +
            "          \"CorrectAnswer\": \"B\",\n" +
            "          \"Explanation\": \"Health insurance premiums are 100% deductible as an adjustment to income on the 1040 for self-employed owners.&nbsp; Otherwise employee premiums paid are on the Schedule C.\",\n" +
            "          \"Question\": \"Which of the following is not a employee tax-free benefit?\",\n" +
            "          \"ExamCaseID\": 0,\n" +
            "          \"SortOrder\": 8,\n" +
            "          \"ExamSectionID\": 0,\n" +
            "          \"PageBreak\": false,\n" +
            "          \"PageBreak2\": false,\n" +
            "          \"ExamSection\": null\n" +
            "        },\n" +
            "        {\n" +
            "          \"ExamQuestionID\": 189616,\n" +
            "          \"CreateDate\": \"2020-06-01T15:50:00\",\n" +
            "          \"AnswerA\": \"Self-employment tax\",\n" +
            "          \"AnswerB\": \"IRA contributions\",\n" +
            "          \"AnswerC\": \"SEP-IRA deduction for self-employed owners\",\n" +
            "          \"AnswerD\": \"Alimony paid (divorce finalized 2018)\",\n" +
            "          \"AnswerE\": \"\",\n" +
            "          \"AnswerF\": \"\",\n" +
            "          \"AnswerI\": \"\",\n" +
            "          \"AnswerII\": \"\",\n" +
            "          \"AnswerIII\": \"\",\n" +
            "          \"AnswerIV\": \"\",\n" +
            "          \"AnswerV\": \"\",\n" +
            "          \"AnswerVI\": \"\",\n" +
            "          \"ExamID\": 4956,\n" +
            "          \"CorrectAnswer\": \"A\",\n" +
            "          \"Explanation\": \"Only 1/2 of the self-employment tax is deductible on the 1040.\",\n" +
            "          \"Question\": \"Which of the following is not an 1040 adjustment to income to calculated AGI?\",\n" +
            "          \"ExamCaseID\": 0,\n" +
            "          \"SortOrder\": 9,\n" +
            "          \"ExamSectionID\": 0,\n" +
            "          \"PageBreak\": false,\n" +
            "          \"PageBreak2\": false,\n" +
            "          \"ExamSection\": null\n" +
            "        },\n" +
            "        {\n" +
            "          \"ExamQuestionID\": 189617,\n" +
            "          \"CreateDate\": \"2020-06-01T15:51:00\",\n" +
            "          \"AnswerA\": \"As an adjustment to income on the 1040 form\",\n" +
            "          \"AnswerB\": \"They are not deductible\",\n" +
            "          \"AnswerC\": \"\",\n" +
            "          \"AnswerD\": \"\",\n" +
            "          \"AnswerE\": \"\",\n" +
            "          \"AnswerF\": \"\",\n" +
            "          \"AnswerI\": \"\",\n" +
            "          \"AnswerII\": \"\",\n" +
            "          \"AnswerIII\": \"\",\n" +
            "          \"AnswerIV\": \"\",\n" +
            "          \"AnswerV\": \"\",\n" +
            "          \"AnswerVI\": \"\",\n" +
            "          \"ExamID\": 4956,\n" +
            "          \"CorrectAnswer\": \"A\",\n" +
            "          \"Explanation\": \"They are another adjustment to income.\",\n" +
            "          \"Question\": \"Are health savings account contributions deductible?\",\n" +
            "          \"ExamCaseID\": 0,\n" +
            "          \"SortOrder\": 10,\n" +
            "          \"ExamSectionID\": 0,\n" +
            "          \"PageBreak\": false,\n" +
            "          \"PageBreak2\": false,\n" +
            "          \"ExamSection\": null\n" +
            "        },\n" +
            "        {\n" +
            "          \"ExamQuestionID\": 189618,\n" +
            "          \"CreateDate\": \"2020-06-01T15:54:00\",\n" +
            "          \"AnswerA\": \"Charitable gifts\",\n" +
            "          \"AnswerB\": \"Property and other taxes up to $10,000\",\n" +
            "          \"AnswerC\": \"Margin interest expense up to net investment income\",\n" +
            "          \"AnswerD\": \"Medical expenses exceeding 7 1/2%\",\n" +
            "          \"AnswerE\": \"\",\n" +
            "          \"AnswerF\": \"\",\n" +
            "          \"AnswerI\": \"\",\n" +
            "          \"AnswerII\": \"\",\n" +
            "          \"AnswerIII\": \"\",\n" +
            "          \"AnswerIV\": \"\",\n" +
            "          \"AnswerV\": \"\",\n" +
            "          \"AnswerVI\": \"\",\n" +
            "          \"ExamID\": 4956,\n" +
            "          \"CorrectAnswer\": \"A\",\n" +
            "          \"Explanation\": \"There is a limit on charitable gifts.&nbsp; All the other answers indicate the limit of the deduction.&nbsp; The answer has to be complete to be true.\",\n" +
            "          \"Question\": \"Which of the following is an allowable itemized deduction on the 1040 Schedule A?\",\n" +
            "          \"ExamCaseID\": 0,\n" +
            "          \"SortOrder\": 11,\n" +
            "          \"ExamSectionID\": 0,\n" +
            "          \"PageBreak\": false,\n" +
            "          \"PageBreak2\": false,\n" +
            "          \"ExamSection\": null\n" +
            "        }\n" +
            "      ]\n" +
            "    },\n" +
            "     {\n" +
            "      \"ExamID\": 4957,\n" +
            "      \"ExamTypeID\": 24,\n" +
            "      \"Randomized\": false,\n" +
            "      \"Timed\": false,\n" +
            "      \"ExamType\": \"Q&A\",\n" +
            "      \"QuestionCount\": 2,\n" +
            "      \"SortOrder\": 0,\n" +
            "      \"ExamName\": \"Income Tax\",\n" +
            "      \"Version\": 20.3,\n" +
            "      \"Active\": true,\n" +
            "      \"ExamModuleID\": 0,\n" +
            "      \"ExamQuestions\": [\n" +
            "        {\n" +
            "          \"ExamQuestionID\": 189619,\n" +
            "          \"CreateDate\": \"2020-06-01T14:36:00\",\n" +
            "          \"AnswerA\": \"Congressional Committee Reports\",\n" +
            "          \"AnswerB\": \"Supreme Court rulings\",\n" +
            "          \"AnswerC\": \"Treasury regulations\",\n" +
            "          \"AnswerD\": \"Internal Revenue Code\",\n" +
            "          \"AnswerE\": \"\",\n" +
            "          \"AnswerF\": \"\",\n" +
            "          \"AnswerI\": \"\",\n" +
            "          \"AnswerII\": \"\",\n" +
            "          \"AnswerIII\": \"\",\n" +
            "          \"AnswerIV\": \"\",\n" +
            "          \"AnswerV\": \"\",\n" +
            "          \"AnswerVI\": \"\",\n" +
            "          \"ExamID\": 4957,\n" +
            "          \"CorrectAnswer\": \"D\",\n" +
            "          \"Explanation\": \"Federal tax law means the Internal Revenue Code.\",\n" +
            "          \"Question\": \"Which is the primary sourse of all tax law?\",\n" +
            "          \"ExamCaseID\": 0,\n" +
            "          \"SortOrder\": 0,\n" +
            "          \"ExamSectionID\": 0,\n" +
            "          \"PageBreak\": false,\n" +
            "          \"PageBreak2\": false,\n" +
            "          \"ExamSection\": null\n" +
            "        },\n" +
            "        {\n" +
            "          \"ExamQuestionID\": 189620,\n" +
            "          \"CreateDate\": \"2020-06-01T15:30:00\",\n" +
            "          \"AnswerA\": \"Step transaction\",\n" +
            "          \"AnswerB\": \"Sham transaction\",\n" +
            "          \"AnswerC\": \"Assignment of income\",\n" +
            "          \"AnswerD\": \"Constructive receipt\",\n" +
            "          \"AnswerE\": \"\",\n" +
            "          \"AnswerF\": \"\",\n" +
            "          \"AnswerI\": \"\",\n" +
            "          \"AnswerII\": \"\",\n" +
            "          \"AnswerIII\": \"\",\n" +
            "          \"AnswerIV\": \"\",\n" +
            "          \"AnswerV\": \"\",\n" +
            "          \"AnswerVI\": \"\",\n" +
            "          \"ExamID\": 4957,\n" +
            "          \"CorrectAnswer\": \"C\",\n" +
            "          \"Explanation\": \"Income <u>paid</u>&nbsp;is key to the answer\",\n" +
            "          \"Question\": \"Which tax law doctrine affects this example?&nbsp; Larry owns L, Inc., an S Corporation.&nbsp; He directs all his income to be paid to his daughter.&nbsp; He reports no income.\",\n" +
            "          \"ExamCaseID\": 0,\n" +
            "          \"SortOrder\": 1,\n" +
            "          \"ExamSectionID\": 0,\n" +
            "          \"PageBreak\": false,\n" +
            "          \"PageBreak2\": false,\n" +
            "          \"ExamSection\": null\n" +
            "        }\n" +
            "      ]\n" +
            "    }\n" +
            "  ]\n" +
            "}";
    public static final String API_BASE_URL = "http://api.kenzahn.com/";
  //  public static final String PAYPAL_CLIENT_ID_ = "AaS8FjMPf21pTy2oR-CLlbuGrVNM6UrWwt2ftR5hGsGpe3Tqym_flpbvnTrrNJkJRPxRpuOcnXfWW-Q1";
    public static final String PAYPAL_CLIENT_ID = "AeAby6hTIwliEVkLbyh0KKoiO01KcqqwuC_Pm5toKafOgcnurRvcMHXElzkl_nqLmI5d4RxGE-K3aUXw";
    private static Retrofit.Builder builder =
            new Retrofit.Builder()
                    .baseUrl(API_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create());

    private static Retrofit retrofit = builder.build();


    public static Retrofit getClient() {
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder().readTimeout(120, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .connectTimeout(20, TimeUnit.SECONDS).build();

        if (retrofit==null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(API_BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static String getCorrectAns(CardContentRes2 res2)
    {
        String correctanswer="";
        String ans=res2.getCorrectAnswer();
        switch (ans)
        {
            case "A":
                correctanswer=res2.getAnswerA();
                break;
            case "B":
                correctanswer=res2.getAnswerB();
                break;
            case "C":
                correctanswer=res2.getAnswerC();
                break;
            case "D":
                correctanswer=res2.getAnswerD();
                break;
        }
        return correctanswer;
    }

    public static String getCorrectAns2(CardContentRes2 res2)
    {
        String correctanswer="";
        String ans=res2.getSelectedAnswer();
        switch (ans)
        {
            case "A":
                correctanswer=res2.getAnswerA();
                break;
            case "B":
                correctanswer=res2.getAnswerB();
                break;
            case "C":
                correctanswer=res2.getAnswerC();
                break;
            case "D":
                correctanswer=res2.getAnswerD();
                break;
        }
        return correctanswer;
    }

}
